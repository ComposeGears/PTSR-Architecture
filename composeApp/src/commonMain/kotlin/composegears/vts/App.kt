package composegears.vts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.rememberNavController
import com.composegears.tiamat.destinations.TiamatGraph
import composegears.vts.screens.home.Home
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(TiamatExperimentalApi::class)
object AppGraph : TiamatGraph

@Composable
@Preview
fun App() {
    MaterialTheme {
        val nc = rememberNavController(startDestination = Home)
        Navigation(nc, AppGraph)
    }
}