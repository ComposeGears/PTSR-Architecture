package composegears.vts

import androidx.compose.runtime.Composable
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.rememberNavController
import com.composegears.tiamat.destinations.TiamatGraph
import composegears.vts.ui.screens.StartScreen


@OptIn(TiamatExperimentalApi::class)
object AppGraph : TiamatGraph

@Composable
fun App() {
    val nc = rememberNavController(startDestination = StartScreen)
    Navigation(nc, AppGraph)
}