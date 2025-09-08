package composegears.vts.screens.items_raw

import composegears.vts.data.AppItem
import kotlinx.coroutines.delay

interface ItemsRawLoadItemsTask {
    suspend fun loadItems(): List<AppItem>
}

class ItemsRawLoadItemsSolution() : ItemsRawLoadItemsTask {
    override suspend fun loadItems(): List<AppItem> {
        delay(3000) // Simulate network delay
        return listOf(
            AppItem(1, "Cat", "A small domesticated carnivorous mammal."),
            AppItem(2, "Dog", "A loyal and friendly domesticated animal."),
            AppItem(3, "Elephant", "The largest land animal with a trunk."),
            AppItem(4, "Lion", "A large wild cat known as the king of the jungle."),
            AppItem(5, "Penguin", "A flightless bird adapted to life in the water.")
        )
    }
}