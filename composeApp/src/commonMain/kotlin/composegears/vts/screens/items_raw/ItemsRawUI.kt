package composegears.vts.screens.items_raw

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
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val ItemsRaw by navDestination {
    val task = remember { ItemsRawLoadItemsSolution() }
    ItemsRawUI(
        task = task,
        onItemClicked = { },
    )
}

@Composable
private fun ItemsRawUI(
    task: ItemsRawLoadItemsTask,
    onItemClicked: (AppItem) -> Unit, // external interaction
) {
    var items: List<AppItem>? by remember(task) { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(task) {
        items = task.loadItems()
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Items Screen")
        Spacer(Modifier.weight(1f))
        if (items == null) Text("Loading...")
        else LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(items!!) {
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
                Button(onClick = {
                    items = null
                    scope.launch { items = task.loadItems() }
                }) {
                    Text("Reload")
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ItemsRawUIPreview() {
    PreviewContent {
        ItemsRawUI(
            task = object : ItemsRawLoadItemsTask {
                override suspend fun loadItems(): List<AppItem> = listOf(
                    AppItem(1, "Cat", "A small domesticated carnivorous mammal."),
                    AppItem(2, "Dog", "A loyal and friendly domesticated animal."),
                    AppItem(3, "Elephant", "The largest land animal with a trunk."),
                )
            },
            onItemClicked = {},
        )
    }
}

