package com.example.demo.notes.tasks

import com.example.demo.notes.models.NoteModel
import kotlinx.coroutines.flow.Flow

/**
 * PTSr — Tasks layer (single-property task → regular interface naming: GetNotesTask).
 * Provides the live stream of all notes.
 *
 * Note: a regular interface is required because the contract is an abstract property
 * (Flow), not a SAM function — fun interface only supports exactly one abstract function.
 *
 * File is named NotesTasks.kt for historical reasons; the class was renamed
 * to GetNotesTask during the Option B refactor (see tasks/info.txt).
 */
interface GetNotesTask {
    val notesFlow: Flow<List<NoteModel>>
}

