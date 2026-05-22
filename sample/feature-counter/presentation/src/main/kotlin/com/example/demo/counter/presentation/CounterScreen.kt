package com.example.demo.counter.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composegears.leviathan.compose.injectAndRetain
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.example.demo.counter.di.CounterDi
import com.example.demo.counter.models.CounterModel
import com.example.demo.counter.tasks.CounterTasks

/**
 * PTSr — Presentation layer: Counter screen.
 *
 * Skill (Tiamat recipe 01): navDestination — turns a @Composable lambda into a screen.
 * Skill (Tiamat recipe 02): navController() + back() — back navigation.
 */
@OptIn(TiamatExperimentalApi::class)
val CounterScreen by navDestination<Unit> {
    val nc = navController()
    CounterScreenBinding(onNavigateBack = { nc.back() })
}

/**
 * PTSr — Binding composable.
 *
 * Skill (Leviathan compose-integration recipe): injectAndRetain — resolves CounterTasks
 *   scoped to the screen's backstack lifetime.
 */
@Composable
private fun CounterScreenBinding(onNavigateBack: () -> Unit) {
    val counterTasks: CounterTasks = injectAndRetain(CounterDi.counterTasks)

    val counter by counterTasks.counterFlow.collectAsState(CounterModel())

    CounterScreenUI(
        counter = counter,
        onIncrement = { counterTasks.increment() },
        onDecrement = { counterTasks.decrement() },
        onReset = { counterTasks.reset() },
        onNavigateBack = onNavigateBack,
    )
}

/**
 * PTSr — Pure UI composable (no Tasks, no Di, no logic).
 *
 * Rules:
 *  - Parameters are only Model types and lambda callbacks.
 *  - Must not import or use any Task, Solution, Di, or Repository type.
 *  - Must not perform state mutations directly — only invoke provided callbacks.
 */
@Composable
fun CounterScreenUI(
    counter: CounterModel,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Counter") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "${counter.count}",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2,
            )

            Spacer(Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onDecrement) { Text("−") }
                Button(onClick = onIncrement) { Text("+") }
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = onReset) { Text("Reset") }
        }
    }
}
