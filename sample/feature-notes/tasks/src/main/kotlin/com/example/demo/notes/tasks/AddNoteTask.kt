package com.example.demo.notes.tasks

/**
 * PTSr — Tasks layer (single-function → VerbNounTask / fun interface naming).
 * A separate task for a separate concern: adding a note.
 */
fun interface AddNoteTask {
    fun addNote(text: String)
}

