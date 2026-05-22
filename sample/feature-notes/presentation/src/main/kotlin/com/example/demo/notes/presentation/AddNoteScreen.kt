package com.example.demo.notes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.leviathan.compose.injectAndRetain
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.example.demo.notes.di.NotesDi
import com.example.demo.notes.tasks.AddNoteTask

/**
 * PTSr — Presentation layer: Add Note screen.
 *
 * Skill (Tiamat recipe 01): navDestination.
 * Skill (Tiamat recipe 02): navController() + back() — return to NotesListScreen when done
 * Skill (Tiamat recipe 20): retain {}
 * Skill (Leviathan compose-integration): injectAndRetain for AddNoteTask.
 */
val AddNoteScreen by navDestination<Unit> {
    val nc = navController()
    AddNoteScreenBinding(onNavigateBack = { nc.back() })
}

/**
 * PTSr — Binding composable.
 *
 * Retains AddNoteScreenData (the mutable input text) and AddNoteTask.
 * Delegates all user actions to AddNoteTask, then pops back to the list.
 */
@Composable
private fun AddNoteScreenBinding(onNavigateBack: () -> Unit) {
    val screenData: AddNoteScreenData = retain { AddNoteScreenData() }

    // Skill (Leviathan compose-integration): injectAndRetain
    val addNoteTask: AddNoteTask = injectAndRetain(NotesDi.addNoteTask)

    AddNoteScreenUI(
        text = screenData.text,
        onTextChange = { screenData.text = it },
        onSave = {
            if (screenData.text.isNotBlank()) {
                addNoteTask.addNote(screenData.text)
                onNavigateBack()
            }
        },
        onNavigateBack = onNavigateBack,
    )
}

/**
 * PTSr — Screen data (state holder for in-flight text input).
 */
class AddNoteScreenData {
    var text: String by mutableStateOf("")
}

/**
 * PTSr — Pure UI composable for adding a note.
 * No Tasks, Di, Solutions, or logic — only state values and callbacks.
 */
@Composable
fun AddNoteScreenUI(
    text: String,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Note") },
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
                .padding(16.dp),
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text("Note text") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5,
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotBlank(),
            ) {
                Text("Save")
            }
        }
    }
}

