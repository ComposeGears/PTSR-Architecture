package composegears.vts.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import composegears.vts.PreviewContent
import composegears.vts.screens.items_flow.ItemsFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val Home by navDestination {
    val nc = navController()
    HomeUI(
        openItemsList = { nc.navigate(ItemsFlow) }
    )
}

@Composable
private fun HomeUI(
    openItemsList: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Screen")
        Spacer(Modifier.weight(1f))
        Button(onClick = openItemsList) {
            Text("Open items list")
        }
        Spacer(Modifier.weight(1f))
    }
}


@Preview
@Composable
private fun HomeUIPreview() {
    PreviewContent {
        HomeUI(openItemsList = {})
    }
}
