package com.example.demo.notes.di

import com.composegears.leviathan.Leviathan
import com.composegears.leviathan.singleton
import com.example.demo.notes.solutions.NotesSolutions
import com.example.demo.notes.tasks.AddNoteTask
import com.example.demo.notes.tasks.DeleteNoteTask
import com.example.demo.notes.tasks.GetNotesTask

/**
 * PTSr — Di layer.
 *
 * Skill: Leviathan "feature-modules" recipe — isolated feature DI object.
 * Skill: Leviathan "interface-binding" recipe — one NotesSolutions instance
 *   is bound to three different task interfaces using inject() to share the singleton.
 *
 * GetNotesTask, AddNoteTask, and DeleteNoteTask are all satisfied by the same
 * NotesSolutions instance, ensuring the in-memory store is shared across all screens.
 */
object NotesDi : Leviathan {

    private val notesSolutions by singleton { NotesSolutions() }

    val getNotesTask by singleton<GetNotesTask> { inject(notesSolutions) }

    val addNoteTask by singleton<AddNoteTask> { inject(notesSolutions) }

    val deleteNoteTask by singleton<DeleteNoteTask> { inject(notesSolutions) }
}


