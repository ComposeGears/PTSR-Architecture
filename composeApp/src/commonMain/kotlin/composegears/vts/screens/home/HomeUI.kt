package composegears.vts.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import composegears.vts.PreviewContent
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val Home by navDestination {
    HomeUI()
}

@Composable
private fun HomeUI() {
    Text("Home")

}


@Preview
@Composable
private fun HomeUIPreview() {
    PreviewContent {
        HomeUI()
    }
}
