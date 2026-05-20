# PTSr Architecture Reference

> **Target audience:** AI agents and developers.  
> AI agents should treat this document as a strict ruleset when generating, reviewing, or refactoring code that follows
> the PTSr pattern.  
> Developers should use it as a design and onboarding guide.

---

## Table of Contents

1. [Overview](#1-overview)
2. [Core Layers](#2-core-layers)
3. [Levels of Abstraction](#3-levels-of-abstraction)
4. [Architecture Elements](#4-architecture-elements)
5. [Naming Conventions](#5-naming-conventions)
6. [PTSr ↔ DDD Mapping](#6-ptsr--ddd-mapping)
7. [Pros & Cons](#7-pros--cons)

---

## 1. Overview

**PTSr** stands for **P**resentation – **T**ask – **S**olution – **r**esult.

The lowercase `r` ("achieved result") is intentional: it is not a separate code layer but the *outcome* of executing a
`Task` through a `Solution`. The name captures the full lifecycle of a user-driven action: the UI (Presentation)triggers
a Task, the Task is fulfilled by a Solution, and a result is reflected back in the UI.

### Core Philosophy

```
"The UI holds state and delegates every action to a Task.
 Tasks are contracts. Solutions are implementations. Di wires them together.
 Nothing leaks across layer boundaries."
```

### Key Constraints (rules for AI agents)

- `Solutions` are **always internal** — they must never be referenced from `Presentation` or exposed outside the feature
  boundary.
- `Presentation` only sees `Models` and `Tasks` (and optionally `Di`). It must not instantiate or import any `Solution`
  class.
- `Tasks` are **interfaces** — the `Presentation` layer is always programming to an abstraction.
- No `ViewModel` or equivalent lifecycle-owner class is used. State is retained via the `retain` mechanism in the
  `Binding` composable.
- The architecture is designed for **Jetpack Compose** UIs only.

---

## 2. Core Layers

### 2.1 Layer Definitions

| Layer                   | Role                                                                                                       | Depends On                         | Visibility                                                                       |
|-------------------------|------------------------------------------------------------------------------------------------------------|------------------------------------|----------------------------------------------------------------------------------|
| **Models**              | Data definitions: data classes, enums, sealed classes, etc.                                                | Nothing                            | Public within feature. Used by Presentation and Tasks.                           |
| **Tasks**               | Interfaces describing what the feature can do. One Task = one concern.                                     | Models (via `api` in multi-module) | Public within feature. Used by Presentation.                                     |
| **Solutions**           | Classes that implement Tasks. Contain repositories, API calls, caches, auto-DI (Hilt), and business logic. | Models, Tasks                      | **INTERNAL ONLY.** Never exposed outside the feature. Only DI module may use it. |
| **Di** *(dependencies)* | Optional. Dependency injection / service-locator wiring layer. Not needed when using auto-DI (Hilt, etc.). | Solutions, Tasks, Models           | Internal. Acts as the composition root of the feature.                           |
| **Presentation**        | Compose UI layer. Holds UI state via retain. Delegates actions to Tasks.                                   | Models, Tasks, Di *(if present)*   | Public. The only layer visible to the outside world.                             |

### 2.2 Dependency Flow

Arrows represent "depends on / uses":

```
          +----------+
          |  Models  |
          +----------+
            ^      ^
            |      |
        +-------+  |
     +->| Tasks |  |
     |  +-------+  |
     |    ^    ^   |
     |    |    |   |
     |    |  +------------+
     |    |  | Solutions  |  (internal)
     |    |  +------------+
     |    |       ^
     |    |       |
     |    +---+---+
     |        |
     |    +───────+
     |    |  Di   |  (optional, internal)
     |    +───────+
     |        ^
     |        |
     |    +────────────────+
     +----|  Presentation  |  (public)
          +────────────────+
```

> Presentation depends on Di (optional) and directly on Tasks; Models are visible transitively via Tasks' `api` dependency.

> Di is optional. When auto-DI (e.g. Hilt) is used, Presentation does not depend on Di directly.

---

## 3. Levels of Abstraction

The architecture defines four levels. Higher levels increase isolation and reusability at the cost of more boilerplate.
Choose the level that matches your project's size and team structure.

| Level  | Name                     | When to Use                          |
|:------:|--------------------------|--------------------------------------|
| **L1** | All in One               | Small features, prototypes, solo dev |
| **L2** | Basic Modularization     | Team features, medium complexity     |
| **L3** | Compact Modularization   | Preference for fewer modules         |
| **L4** | Shareable Modularization | Cross-feature shared contracts       |

---

### Level 1 — All in One

All code lives in a single Gradle module. Layer boundaries are enforced through Kotlin **visibility modifiers** (
`internal`, `private`), not module boundaries.

**Rule:** Only `Presentation` types (and any `Models` they expose in their public signatures) are `public`. All other
layers are `internal` or `private`.

**Package structure** (feature root: `com.example.feature`):

```
com.example.feature/
  ├── models/          (internal)  -- data classes, enums, sealed classes
  ├── tasks/           (internal)  -- interfaces
  ├── solutions/       (internal)  -- implementations, repositories
  ├── di/              (internal)  -- optional service locator
  └── presentation/   (public)    -- Composable screens, Binding + UI
```

---

### Level 2 — Basic Modularization

Code is split into separate Gradle modules. Only the `feature` (root) module is a dependency for other features. The root
module aggregates all sub-modules.

The `feature` module depends on all sub-modules and is the public entry point for the feature. It exposes only the `presentation` module.

**Rule:** All sub-modules except `presentation` are `internal` to the feature and must not appear in external `api` or
`implementation` dependency graphs.

**Module structure** (feature root: `com.example.feature`):

```
feature/
  ├── models/                    com.example.feature.models
  ├── tasks/                     com.example.feature.tasks
  ├── solutions/                 com.example.feature.solutions
  ├── di/                        com.example.feature.di             [optional]
  └── presentation/              com.example.feature.presentation
```

**Gradle dependency declaration for `tasks` module:**

```kotlin
// feature/tasks/build.gradle.kts
dependencies {
    api(project(":feature:models"))  // 'api' so Presentation can see Models via Tasks
}

// feature/solutions/build.gradle.kts
dependencies {
    implementation(project(":feature:tasks"))
}

// feature/di/build.gradle.kts
dependencies {
    implementation(project(":feature:tasks"))  // Models are transitively visible via 'api'
    implementation(project(":feature:solutions"))  // Di needs to know about Solutions to wire them
}

// feature/presentation/build.gradle.kts
dependencies {
    implementation(project(":feature:tasks"))  // Models are transitively visible via 'api'
    implementation(project(":feature:di"))     // optional
}
```

---

### Level 3 — Compact Modularization

Follows all rules of Level 2. Two structural changes:

1. `models` + `tasks` are **merged** into a single `domain` module with two sub-packages.
2. `solutions` is **renamed** to `data` to better reflect its repository/data-source role.

**Module structure:**

```
feature/
  ├── domain/          com.example.feature.domain
  │     ├── models/    com.example.feature.domain.models
  │     └── tasks/     com.example.feature.domain.tasks
  ├── data/            com.example.feature.data         [INTERNAL, replaces solutions]
  ├── di/              com.example.feature.di           [optional]
  └── presentation/    com.example.feature.presentation
```

---

### Level 4 — Shareable Modularization

Follows all rules of Level 2. Adds a set of `shared-*` modules that expose **contracts** (Models + Tasks) usable by
multiple (other) features. Shared modules never contain solution/implementation code — implementations
always live in the `solutions` (internal) module.

**Additional modules:**

```
feature/
  ├── shared-models/       com.example.feature.shared.models
  ├── shared-tasks/        com.example.feature.shared.tasks
  ├── shared-di/           com.example.feature.shared.di    [optional]
  ├── shared-presentation/ com.example.feature.shared.presentation
  │
  ├── models/              com.example.feature.models
  ├── tasks/               com.example.feature.tasks        (depends on shared-tasks via api)
  ├── solutions/           com.example.feature.solutions    [INTERNAL]
  ├── di/                  com.example.feature.di           [optional]
  └── presentation/        com.example.feature.presentation
```

**Key rules for L4:**
1. `*` module depends on `shared-*` module using `api`, making shared contracts visible transitively.
2. `shared-di` depends on `solutions` to wire shared Tasks to their implementations.

---

## 4. Architecture Elements

### 4.1 Retain Mechanism

PTSr uses a **retain** mechanism to keep UI state alive for the duration of a screen's presence in the back stack. When
the screen leaves the back stack the retained data is released.

```
Screen in back stack  →  retained data is alive
Screen popped         →  retained data is released
```

- `retain { }` creates or retrieves an instance that survives recompositions and configuration changes but is scoped to
  the screen lifecycle.
- Long-living data producers (network streams, timers) use `Flow`, `produceState`, or `produceRetainedState`.
- `inputs` (parameters passed from the previous screen) are **not retained** — they are only used to initialise retained
  state.

> **Library note:** The base `retain { }` function is part of the Kotlin/Compose ecosystem and available without extra dependencies.
> Extended retain helpers such as `produceRetainedState` may require an external library
> (e.g. [composegears/Tiamat](https://github.com/ComposeGears/Tiamat), [Voyager](https://github.com/adrielcafe/voyager)).

### 4.2 Screen Pattern: Binding + UI

Every screen has **at least two composable functions**:

```
+---------------------------+      +---------------------------+
|   FeatureScreenBinding    |      |    FeatureScreenUI        |
|---------------------------|      |---------------------------|
| - Retains state           | ---> | - Pure UI only            |
| - Resolves Tasks via Di   |      | - Models as params        |
| - Calls FeatureScreenUI   |      | - Callbacks as params     |
| - Owns lifecycle logic    |      | - No logic, no Tasks      |
+---------------------------+      | - No state mutation       |
                                   +---------------------------+
```

**Rules for `Binding`:**

- May retain `ScreenData` and `Task` instances.
- Receives `inputs` (non-retained, from navigation) as parameters.
- Wires callbacks by delegating to retained Tasks.

**Rules for `UI`:**

- Parameters are only `Model` types and lambda callbacks.
- Must not import or use any `Task`.
- Must not perform state mutations directly — only invoke provided callbacks.

### 4.3 No ViewModels

PTSr **does not use ViewModels** (or equivalent lifecycle-owner constructs). The reasons:

| ViewModel concern         | PTSr equivalent                               |
|---------------------------|-----------------------------------------------|
| Survive config changes    | `retain { }` in Binding composable            |
| Expose state to UI        | Retained `ScreenData` passed to UI composable |
| Execute business logic    | `Task` interface called from Binding callback |
| Scope to screen lifecycle | `retain` scoped to back-stack entry           |

ViewModels add an extra indirection layer that PTSr eliminates by retaining data and tasks directly in the composable
scope.

### 4.4 Code Example — Screen Binding & UI

```kotlin
// ── Navigation host (caller) ──────────────────────────────────────────────
<NavigationHost > {
    val inputs = ... // e.g. route arguments: productId, userId
    FeatureScreenBinding(inputs)
}

// ── Binding composable ────────────────────────────────────────────────────
// Responsibilities:
//   1. Initialise and retain screen state from inputs.
//   2. Retain Task instances (resolved via Di or auto-DI).
//   3. Expose callbacks that delegate to Tasks.
//   4. Call the pure UI composable.
@Composable
fun FeatureScreenBinding(input: FeatureInput) {

    // Retained for the screen's lifetime; inputs only used for initialisation.
    val uiData: FeatureScreenData = retain { FeatureScreenData(input) }

    // Task retained alongside the data.
    val doSomethingTask: DoSomethingTask = retain { Di.provideDoSomethingTask() }

    // Long-living producer example:
    val liveItems by produceRetainedState(initialValue = emptyList()) {
        Di.provideGetItemsTask().itemsFlow().collect { value = it }
    }

    FeatureScreenUI(
        uiData = uiData,
        items = liveItems,
        onDoSomething = { doSomethingTask.doSomething(uiData.selectedItem) },
        onNavigateBack = { /* navigation call */ }
    )
}

// ── Pure UI composable ────────────────────────────────────────────────────
// Responsibilities:
//   - Render Models.
//   - Call provided callbacks on user interaction.
// Forbidden:
//   - Any import of Task, Solution, Di, or Repository types.
//   - Any direct state mutation or business logic.
@Composable
fun FeatureScreenUI(
    uiData: FeatureScreenData,
    items: List<ItemModel>,
    onDoSomething: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column {
        TopBar(onBack = onNavigateBack)
        ItemList(items = items)
        Button(onClick = onDoSomething) {
            Text("Do Something with ${uiData.selectedItem.name}")
        }
    }
}
```

### 4.5 Code Example — Task & Solution Pair

Demonstrates naming conventions, `fun interface` usage, internal repository encapsulation, and Di wiring.

```kotlin
// ── models/UserModel.kt ───────────────────────────────────────────────────
data class UserModel(
    val id: String,
    val name: String,
    val email: String
)

// ── tasks/GetUserTask.kt ──────────────────────────────────────────────────
// Single-function task: use 'fun interface' for SAM conversion convenience.
// Naming: VerbNounTask
fun interface GetUserTask {
    suspend fun getUser(userId: String): UserModel
}

// ── tasks/UserTasks.kt ────────────────────────────────────────────────────
// Multi-function task for the same concern (User): use NounTasks naming.
interface UserTasks {
    suspend fun getUser(userId: String): UserModel
    suspend fun updateUser(user: UserModel)
    suspend fun deleteUser(userId: String)
}

// ── solutions/UserRepository.kt ──────────────────────────────────────────
// Internal to the solutions layer. Never referenced from Presentation or Di.
internal class UserRepository(private val api: UserApi) {
    suspend fun fetchUser(userId: String): UserModel = api.getUser(userId)
    suspend fun saveUser(user: UserModel) = api.putUser(user)
    suspend fun removeUser(userId: String) = api.deleteUser(userId)
}

// ── solutions/UserSolutions.kt ───────────────────────────────────────────
// Implements UserTasks. Depends on internal repository.
// Naming: NounSolutions (multi-function) or VerbNounSolution (single-function).
internal class UserSolutions(
    private val repository: UserRepository
) : UserTasks {

    override suspend fun getUser(userId: String): UserModel =
        repository.fetchUser(userId)

    override suspend fun updateUser(user: UserModel) =
        repository.saveUser(user)

    override suspend fun deleteUser(userId: String) =
        repository.removeUser(userId)
}

// Single-function solution example:
internal class GetUserSolution(
    private val repository: UserRepository
) : GetUserTask {
    override suspend fun getUser(userId: String): UserModel =
        repository.fetchUser(userId)
}

// ── di/UserDi.kt ──────────────────────────────────────────────────────────
// Service locator wiring. The only place that knows about Solutions.
object UserDi {
    private val api by lazy { UserApi() }
    private val repository by lazy { UserRepository(api) }

    fun provideUserTasks(): UserTasks = UserSolutions(repository)
    fun provideGetUserTask(): GetUserTask = GetUserSolution(repository)
}

// ── Presentation usage (Binding) ──────────────────────────────────────────
@Composable
fun UserScreenBinding(userId: String) {
    val userTasks: UserTasks = retain { UserDi.provideUserTasks() }
    val uiData: UserScreenData = retain { UserScreenData() }

    RetainedEffect(userId) {
        uiData.user = userTasks.getUser(userId)
    }

    UserScreenUI(
        user = uiData.user,
        onDelete = { userTasks.deleteUser(userId) }
    )
}
```

---

## 5. Naming Conventions

### 5.1 Tasks

| Situation                                       | Pattern                               | Examples                                                              |
|-------------------------------------------------|---------------------------------------|-----------------------------------------------------------------------|
| Task with **one** function                      | `VerbNounTask` — use `fun interface`  | `GetUserTask`, `UpdateUserTask`, `DeleteUserTask`, `FetchProductTask` |
| Task with **multiple** functions (same concern) | `NounTasks` — regular `interface`     | `UserTasks`, `ProductTasks`, `OrderTasks`                             |
| ❌ Mixed concerns in one Task                    | Violation — split into separate tasks | ~~`UserAndProductTask`~~, ~~`GetUserAndOrderTask`~~                   |

### 5.2 Solutions

| Situation                                       | Pattern                       | Examples                                                                              |
|-------------------------------------------------|-------------------------------|---------------------------------------------------------------------------------------|
| Implements a single-function Task               | `VerbNounSolution`            | `GetUserSolution`, `UpdateUserSolution`, `DeleteUserSolution`, `FetchProductSolution` |
| Implements a multi-function Task (same concern) | `NounSolutions`               | `UserSolutions`, `ProductSolutions`, `OrderSolutions`                                 |

### 5.3 Screens and Composables

| Part                       | Pattern                            |
|----------------------------|------------------------------------|
| Binding composable         | `FeatureNameScreenBinding(inputs)` |
| Pure UI composable         | `FeatureNameScreenUI(...)`         |
| Screen data holder (state) | `FeatureNameScreenData`            |

---

## 6. PTSr ↔ DDD Mapping

PTSr maps 1-to-1 onto standard DDD layers.

| PTSr                     | DDD Layer                | Notes                                                                                      |
|--------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| **Models** + **Tasks**   | Domain Layer             | Models = domain data; Tasks = use-case contracts (stateless interfaces, no implementation) |
| **Solutions**            | Data Layer               | Repositories, API clients, caches — always internal, never exposed                         |
| **Di**                   | Infrastructure           | Wires Tasks to Solutions; optional when using auto-DI (Hilt, etc.)                         |
| **Presentation**         | Presentation Layer       | Compose UI only; holds retained state instead of a ViewModel                               |

---

## 7. Pros & Cons

### 7.1 Pros

| # | Advantage                        | Detail                                                                                                                                                 |
|:-:|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1 | **Clear separation of concerns** | Each layer has one job. No logic bleeds into UI; no UI knowledge leaks into the data layer.                                                            |
| 2 | **High testability**             | Tasks are interfaces — trivially mockable. Solutions can be unit-tested without UI. Binding composables are thin and predictable.                      |
| 3 | **No ViewModel boilerplate**     | Eliminates the ViewModel class, its Factory, and its `SavedStateHandle` plumbing. State is retained directly in composable scope.                      |
| 4 | **Lifecycle-safe retain**        | Retained objects survive recompositions and configuration changes. Released automatically when the screen is popped from the back stack.               |
| 5 | **Scalable abstraction levels**  | Start at L1 for a small feature; migrate to L2/L3/L4 incrementally as the codebase grows. No big-bang refactor required.                               |
| 6 | **Safe public surface**          | Only Presentation (and its Model parameters) is public. Solutions are always internal. Consumers cannot accidentally couple to implementation details. |
| 7 | **AI-agent friendly**            | Strict layer rules and naming conventions make it straightforward for AI agents to generate, review, and validate conforming code automatically.       |

### 7.2 Cons

| # | Trade-off                             | Detail                                                                                                                                                      |
|:-:|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1 | **Compose-only**                      | The retain mechanism and Binding/UI pattern assume Jetpack Compose. Not applicable to XML/View-based UI.                                                    |
| 2 | **L4 module proliferation**           | Shareable Modularization creates many Gradle modules per feature. Increases build configuration overhead and IDE indexing time on large projects.           |
