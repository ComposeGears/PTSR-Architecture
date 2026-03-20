package composegears.vts.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.produceRetainedState
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import composegears.vts.ui.components.TextLabel

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val StartScreen by navDestination {
    val provideDataSolution = remember<ProvideDataTask> { ProvideDataSolution() }
    SomeElement { provideDataSolution }
}

// ------ UI --------

@Composable
fun SomeElement(
    provideDataSolution: SolutionFor<ProvideDataTask>
) {
    SomeTaskFun(provideDataSolution())
}

@Composable
@OptIn(TiamatExperimentalApi::class)
fun SomeTaskFun(
    provideData: ProvideDataTask // task or or solution provider
) {
    val data by produceRetainedState(0) {
        value = provideData.provideData()
    }
    TextLabel("Data: $data")
}

//------ task & solution for task ------

typealias SolutionFor<T> = () -> T

interface ProvideDataTask {
    fun provideData(): Int
}

class ProvideDataSolution : ProvideDataTask {
    override fun provideData(): Int {
        return 42
    }
}

//---- Preview ----
@Preview
@Composable
fun StartScreenPreview() {
    SomeElement(provideDataSolution = {
        object : ProvideDataTask {
            override fun provideData(): Int {
                return 42
            }
        }
    })
}