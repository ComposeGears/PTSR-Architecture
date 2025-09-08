package composegears.vts.screens.items_flow

import composegears.vts.data.AppItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface ItemsFlowLoadItemsTask {

    val state: StateFlow<State>

    fun loadItems()

    sealed interface State
    data object Loading : State
    data class Success(val items: List<AppItem>) : State
}

class ItemsFlowLoadItemsSolution(
    private val scope: CoroutineScope,
    previousState: ItemsFlowLoadItemsTask.State = ItemsFlowLoadItemsTask.Loading
) : ItemsFlowLoadItemsTask {

    private val _state = MutableStateFlow(previousState)
    override val state: StateFlow<ItemsFlowLoadItemsTask.State> = _state.asStateFlow()

    init {
        loadItems()
    }

    override fun loadItems() {
        scope.launch {
            _state.emit(ItemsFlowLoadItemsTask.Loading)
            delay(3000) // Simulate network delay
            _state.emit(
                ItemsFlowLoadItemsTask.Success(
                    listOf(
                        AppItem(1, "Cat", "A small domesticated carnivorous mammal."),
                        AppItem(2, "Dog", "A loyal and friendly domesticated animal."),
                        AppItem(3, "Elephant", "The largest land animal with a trunk."),
                        AppItem(4, "Lion", "A large wild cat known as the king of the jungle."),
                        AppItem(5, "Penguin", "A flightless bird adapted to life in the water.")
                    )
                )
            )
        }
    }
}