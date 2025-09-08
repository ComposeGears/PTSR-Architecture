package composegears.vts.screens.items_flow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import composegears.vts.PreviewContent
import composegears.vts.data.AppItem
import composegears.vts.screens.items_flow.ItemsFlowLoadItemsTask.Loading
import composegears.vts.screens.items_flow.ItemsFlowLoadItemsTask.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val ItemsFlow by navDestination {
    val scope = rememberCoroutineScope()
    val task = remember { ItemsFlowLoadItemsSolution(scope) }
    ItemsFlowUI(
        task = task,
        onItemClicked = { },
    )
}

@Composable
private fun ItemsFlowUI(
    task: ItemsFlowLoadItemsTask,
    onItemClicked: (AppItem) -> Unit, // external interaction
) {
    val state by task.state.collectAsState()
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Items Screen")
        Spacer(Modifier.weight(1f))
        when (val s = state) {
            Loading -> Text("Loading...")
            is Success -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(s.items) {
                    Column(
                        Modifier.border(1.dp, color = Color.Black).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(it.name)
                        Text(it.description)
                        Button(onClick = { onItemClicked(it) }) {
                            Text("See more >>")
                        }
                    }
                }
                item {
                    Button(onClick = task::loadItems) {
                        Text("Reload")
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ItemsFlowUIPreview() {
    PreviewContent {
        ItemsFlowUI(
            task = object : ItemsFlowLoadItemsTask {
                override val state: StateFlow<ItemsFlowLoadItemsTask.State> = MutableStateFlow(
                    Success(
                        listOf(
                            AppItem(1, "Cat", "A small domesticated carnivorous mammal."),
                            AppItem(2, "Dog", "A loyal and friendly domesticated animal."),
                            AppItem(3, "Elephant", "The largest land animal with a trunk."),
                        )
                    )
                )

                override fun loadItems() {}
            },
            onItemClicked = {},
        )
    }
}

