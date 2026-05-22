package com.example.demo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.rememberNavController
import com.example.demo.counter.presentation.CounterScreen
import com.example.demo.home.HomeScreen
import com.example.demo.notes.presentation.AddNoteScreen
import com.example.demo.notes.presentation.NotesListScreen

/**
 * Application entry point.
 *
 * Skill (Tiamat recipe 01 — simple screen + Navigation host):
 *   rememberNavController sets the start destination; Navigation renders the current screen.
 *   All reachable destinations must be registered in the destinations array.
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 480.dp, height = 720.dp),
        title = "PTSr Demo",
    ) {
        App()
    }
}

@Composable
private fun App() {
    MaterialTheme {
        val navController = rememberNavController(startDestination = HomeScreen)

        Navigation(
            navController = navController,
            destinations = arrayOf(
                HomeScreen,
                CounterScreen,
                NotesListScreen,
                AddNoteScreen,
            ),
        )
    }
}

