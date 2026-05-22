package com.example.demo.counter.solutions

import com.example.demo.counter.models.CounterModel
import com.example.demo.counter.tasks.CounterTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * PTSr — Solutions layer (multi-function → NounSolutions naming).
 *
 * Isolation note: this class is NOT referenced from Presentation or outside the feature.
 * Enforcement is via Gradle module dependency graph (L2 modularization):
 * the feature-counter aggregator only re-exports :feature-counter:presentation via api.
 */
class CounterSolutions : CounterTasks {

    private val _counterFlow = MutableStateFlow(CounterModel())

    override val counterFlow: Flow<CounterModel> = _counterFlow.asStateFlow()

    override fun increment() = _counterFlow.update { it.copy(count = it.count + 1) }

    override fun decrement() = _counterFlow.update { it.copy(count = it.count - 1) }

    override fun reset() {
        _counterFlow.value = CounterModel()
    }
}

