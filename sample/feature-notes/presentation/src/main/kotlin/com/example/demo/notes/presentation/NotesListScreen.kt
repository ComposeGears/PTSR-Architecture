package com.example.demo.notes.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.leviathan.compose.injectAndRetain
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.example.demo.notes.di.NotesDi
import com.example.demo.notes.models.NoteModel
import com.example.demo.notes.tasks.DeleteNoteTask
import com.example.demo.notes.tasks.GetNotesTask

/**
 * PTSr — Presentation layer: Notes list screen.
 *
 * Skill (Tiamat recipe 01): navDestination.
 * Skill (Tiamat recipe 02): navigate(AddNoteScreen) — forward navigation within the feature.
 * Skill (Leviathan compose-integration): injectAndRetain for GetNotesTask and DeleteNoteTask.
 */
@OptIn(TiamatExperimentalApi::class)
val NotesListScreen by navDestination<Unit> {
    val nc = navController()
    NotesListScreenBinding(
        onNavigateBack = { nc.back() },
        onNavigateToAddNote = { nc.navigate(AddNoteScreen) },
    )
}

@Composable
private fun NotesListScreenBinding(
    onNavigateBack: () -> Unit,
    onNavigateToAddNote: () -> Unit,
) {
    val getNotesTask: GetNotesTask = injectAndRetain(NotesDi.getNotesTask)
    val deleteNoteTask: DeleteNoteTask = injectAndRetain(NotesDi.deleteNoteTask)

    val notes by getNotesTask.notesFlow.collectAsState(emptyList())

    NotesListScreenUI(
        notes = notes,
        onDeleteNote = { id -> deleteNoteTask.deleteNote(id) },
        onNavigateToAddNote = onNavigateToAddNote,
        onNavigateBack = onNavigateBack,
    )
}

/**
 * PTSr — Pure UI composable for the notes list.
 * No Tasks, Di, Solutions, or logic — only Models and callbacks.
 */
@Composable
fun NotesListScreenUI(
    notes: List<NoteModel>,
    onDeleteNote: (id: String) -> Unit,
    onNavigateToAddNote: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddNote) {
                Icon(Icons.Filled.Add, contentDescription = "Add note")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("No notes yet. Tap + to add one.", style = MaterialTheme.typography.body1)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteItem(note = note, onDelete = { onDeleteNote(note.id) })
                }
            }
        }
    }
}

@Composable
private fun NoteItem(note: NoteModel, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = 2.dp) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = note.text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.body1,
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete note")
            }
        }
    }
}
