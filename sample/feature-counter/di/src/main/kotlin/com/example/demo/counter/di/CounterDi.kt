package com.example.demo.counter.di

import com.composegears.leviathan.Leviathan
import com.composegears.leviathan.singleton
import com.example.demo.counter.solutions.CounterSolutions
import com.example.demo.counter.tasks.CounterTasks

/**
 * PTSr — Di layer.
 *
 * Skill: Leviathan "feature-modules" recipe — each feature owns its own Leviathan object.
 * Skill: Leviathan "interface-binding" recipe — singleton<Interface> { Implementation() }
 *   binds CounterSolutions to the CounterTasks contract without leaking the implementation type.
 *
 * This object is the ONLY place that knows CounterSolutions exists.
 * It is referenced only from :feature-counter:presentation (via implementation dependency).
 */
object CounterDi : Leviathan {

    /**
     * Singleton: lives for the life of CounterDi (the object = application lifetime).
     * The MutableStateFlow inside CounterSolutions must survive screen navigation,
     * so a singleton scope is appropriate here.
     */
    val counterTasks by singleton<CounterTasks> { CounterSolutions() }
}


