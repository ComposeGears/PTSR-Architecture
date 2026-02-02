package composegears.vts.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.retain.retain

class Trigger(
    private val onTriggered: () -> Unit
) : () -> Unit {
    override fun invoke() {
        onTriggered()
    }

    fun trigger() {
        onTriggered()
    }
}

internal class TriggerHolder {
    val triggerState = mutableStateOf(
        value = Trigger(this::trigger),
        policy = neverEqualPolicy()
    )

    fun trigger() {
        triggerState.value = Trigger(this::trigger)
    }
}

@Composable
fun  retainTrigger(): Trigger {
    val triggerHolder = retain { TriggerHolder() }
    return triggerHolder.triggerState.value
}