package com.example.demo.notes.solutions

import com.example.demo.notes.models.NoteModel
import com.example.demo.notes.tasks.AddNoteTask
import com.example.demo.notes.tasks.DeleteNoteTask
import com.example.demo.notes.tasks.GetNotesTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * PTSr — Solutions layer (NounSolutions naming, implements three separate fun interfaces).
 *
 * NotesSolutions implements GetNotesTask, AddNoteTask, and DeleteNoteTask — a single in-memory
 * store serves all read and write operations. Only Di knows about this class.
 */
class NotesSolutions : GetNotesTask, AddNoteTask, DeleteNoteTask {

    private val _notesFlow = MutableStateFlow<List<NoteModel>>(emptyList())

    override val notesFlow: Flow<List<NoteModel>> = _notesFlow.asStateFlow()

    override fun addNote(text: String) {
        val note = NoteModel(id = UUID.randomUUID().toString(), text = text.trim())
        _notesFlow.update { it + note }
    }

    override fun deleteNote(id: String) =
        _notesFlow.update { notes -> notes.filter { it.id != id } }
}

