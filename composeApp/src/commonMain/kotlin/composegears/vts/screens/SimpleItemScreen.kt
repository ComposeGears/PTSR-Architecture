@file:OptIn(TiamatExperimentalApi::class)

package composegears.vts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.composegears.leviathan.Leviathan
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navArgsOrNull
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.ui.tooling.preview.Preview


// ----- Domain Layer -----

data class Item(
    val id: Int,
    val name: String,
    val description: String,
)

sealed interface Status {
    data object Loading : Status
    data object Failed : Status
    data class Success(val item: Item) : Status
}

interface LoadItemTask {
    fun loadItem(itemId: Int): Flow<Status>
}

// ----- Data Layer -----

class LoadItemSolution : LoadItemTask {
    override fun loadItem(itemId: Int): Flow<Status> = flow {
        emit(Status.Loading)
        delay(3000)
        emit(Status.Success(Item(itemId, "Item #$itemId", "This is item #$itemId")))
    }
}

// ----- Infrastructure Layer -----

object DI : Leviathan() {
    val loadItemTask by factoryOf<LoadItemTask> { LoadItemSolution() }
}

// ----- Presentation Layer -----

@InstallIn(AppGraph::class)
val SimpleItemDestination by navDestination<Int> {
    val itemId = navArgsOrNull() ?: 1
    val loadItemTask = inject { DI.loadItemTask }
    val itemData by produceState<Status>(Status.Loading, itemId) {
        loadItemTask.loadItem(itemId).collect {
            value = it
        }
    }
    SimpleItemUI(
        itemData = itemData,
        onItemClicked = {}
    )
}

@Composable
private fun SimpleItemUI(
    itemData: Status,
    onItemClicked: (Item) -> Unit,
) {

    Box(
        modifier = Modifier.background(Color.White).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (itemData) {
            is Status.Loading -> {
                CircularProgressIndicator()
            }
            is Status.Failed -> {
                Text("Failed to load item")
            }
            is Status.Success -> {
                val item = itemData.item
                Column(
                    Modifier.clickable(true, onClick = { onItemClicked(item) })
                ) {
                    Text("Item ID: ${item.id}")
                    Text("Item Name: ${item.name}")
                    Text("Item Description: ${item.description}")
                }
            }
        }
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun SimpleItemLoadingPreview() {
    SimpleItemUI(
        itemData = Status.Loading,
        onItemClicked = {}
    )
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun SimpleItemErrorPreview() {
    SimpleItemUI(
        itemData = Status.Failed,
        onItemClicked = {}
    )
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun SimpleItemItemPreview() {
    SimpleItemUI(
        itemData = Status.Success(Item(1, "Item #$1", "This is item #$1")),
        onItemClicked = {}
    )
}
