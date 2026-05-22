package com.example.demo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.example.demo.counter.presentation.CounterScreen
import com.example.demo.notes.presentation.NotesListScreen

/**
 * PTSr L1 — Home screen lives in the app module.
 * L1 applies because there is no business logic: the screen is pure navigation.
 * Visibility modifiers (private Binding) enforce the Binding+UI split within the single module.
 *
 * Skill (Tiamat recipe 01): navDestination — screen declaration.
 * Skill (Tiamat recipe 02): navController() + navigate() — forward navigation to features.
 */
val HomeScreen by navDestination<Unit> {
    val nc = navController()

    // Binding (L1: private, same file/module — no Tasks or Solutions to retain)
    HomeScreenBinding(
        onOpenCounter = { nc.navigate(CounterScreen) },
        onOpenNotes = { nc.navigate(NotesListScreen) },
    )
}

/**
 * PTSr — Binding composable.
 * No retain needed: the home screen holds no retained state.
 * Navigation lambdas are passed through directly from navDestination.
 */
@Composable
private fun HomeScreenBinding(
    onOpenCounter: () -> Unit,
    onOpenNotes: () -> Unit,
) {
    HomeScreenUI(
        onOpenCounter = onOpenCounter,
        onOpenNotes = onOpenNotes,
    )
}

/**
 * PTSr — Pure UI composable for the home/menu screen.
 * No Tasks, Di, or logic — purely renders two navigation entry-points.
 */
@Composable
fun HomeScreenUI(
    onOpenCounter: () -> Unit,
    onOpenNotes: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("PTSr Demo") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Choose a feature:", style = MaterialTheme.typography.h6)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onOpenCounter,
                modifier = Modifier.fillMaxWidth(0.6f),
            ) {
                Text("Counter")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onOpenNotes,
                modifier = Modifier.fillMaxWidth(0.6f),
            ) {
                Text("Notes")
            }
        }
    }
}
