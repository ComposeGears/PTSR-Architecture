## Work in progress

# Task-Solution-Presentation

Is an prototype architecture made to bypass overengineared solutions like CleanArchitecture

Why? => I'v see to many modern app's that use 100500 ViewModels, ViewModel for a View... 1kk UseCases, complex DI, events... KEEP IT SIMPLE...

My aim is to MAKE IT SIMPLE!! Any arch should be simple, intuitive, understandable, extendable, maintainable and once again SIMPLE

No libraries, no dependencies, any one should be able to do make it and use it...

Let's take a look onto real life case:

I need to make a tea -> What is it?? ->  it is a `Task`

What do i need to do? -> I need Solution

What will i use? -> Cup, Teapot, Water, Heater, Tea-Leafs

Steps:
Take Teapot -> fill-in with water -> heat (this is sub-task) -> combine with leafs -> provide result in the cup

--------

Now lets make same with a code

```kotlin
interface MakeATeaTask(){
	fun makeATea():CupOfTea
}
```

```kotlin
class MakeATeaSolution(
		val boilTheWaterTask : BoilTheWaterTask // here we have another task / sub-task	
	):MakeATeaTask{
	
	fun makeATea(
		cup:Cup,     // our data
		leafs:Leafs, // ummm
		water:Water, // another data
		teapot:Pot,  // and last one data
	):CupOfTea{
		val bioledWater = boilTheWaterTask.boil(water, teapo)
		cup.clear()		
		return CupOfTea(cup, boiledWater, leafs)
	}
}
```

```kotlin
@Composable
fun TeaScreen(){
	// here we create `tasks` and bind it with UI
	val task:MakeATeaTask = remember {/*some di provides MakeATeaSolution*/}
	val cupOfTea = rememberSaveable { task.makeATea(...) } // retain
	TeaScreenUI(cupOfTea)
}

@Composable
fun TeaScreenUI(cupOfTea : CupOfTea){
...
}

@Preview
@Composable
fun TeaScreenUIPreview(){
	TeaScreenUI(SomeTestCupOfTea())
}

```

----------------------------

In the more complex cases we can make a `Task` to be a `flow<Status>` + operations
```kotlin
interface MakeATeaTask():Flow<CupOfTam>{
	fun makeATea(inputs)
}
```
