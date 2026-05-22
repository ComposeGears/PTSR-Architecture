package com.example.demo.counter.tasks

import com.example.demo.counter.models.CounterModel
import kotlinx.coroutines.flow.Flow

/**
 * PTSr — Tasks layer (multi-function, same concern → NounTasks naming).
 * This is the contract between Presentation and Solutions.
 * Presentation never sees the implementing class.
 */
interface CounterTasks {
    /** Live stream of the current counter state. */
    val counterFlow: Flow<CounterModel>

    fun increment()
    fun decrement()
    fun reset()
}

