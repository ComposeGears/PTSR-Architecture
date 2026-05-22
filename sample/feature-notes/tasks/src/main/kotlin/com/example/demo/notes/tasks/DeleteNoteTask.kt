package com.example.demo.notes.tasks

/**
 * PTSr — Tasks layer (single-function → VerbNounTask / fun interface naming).
 * Deletes a single note by its identifier.
 */
fun interface DeleteNoteTask {
    fun deleteNote(id: String)
}

