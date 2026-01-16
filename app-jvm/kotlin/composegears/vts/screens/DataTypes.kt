@file:OptIn(TiamatExperimentalApi::class)

package composegears.vts.screens

import androidx.compose.runtime.*
import androidx.compose.runtime.retain.retain
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.produceRetainedState
import kotlinx.coroutines.flow.flowOf

object OneShotSolution {
    fun getData() = 1
}

object FlowSolution {
    fun getData() = flowOf(1, 2, 3)
}

@Composable
fun DataTypes() {
    // # 1 on-the-screen
    //     the data holds/collected till the screen is opened
    //     re-collect/re-request upon re-opening

    // # 1-1 on-the-screen-one-shot
    val onTheScreenOneShot = remember { OneShotSolution.getData() }
    // # 1-2 on-the-screen-flow
    val onTheScreenFlow1 by produceState(0) {
        FlowSolution.getData().collect { value = it }
    }
    val onTheScreenFlow2 by FlowSolution.getData().collectAsState(0)


    // # 2 on-the-stack
    //     the data holds/retained till the screen is removed from the back stack
    //     re-request upon re-adding to the back stack
    //     NOT survive process re-creation

    // # 2-1 on-the-stack-one-shot
    val onTheStackOneShot = retain { OneShotSolution.getData() }
    // # 2-2 on-the-stack-flow | collect till on screen
    val onTheStackFlowScreenScoped by produceRetainedState(0) {
        // todo FlowSolution.getData().collect { value = it }
    }
    val t by produceRetainedState(0) {
        FlowSolution
            .getData()
            .collect {
                value = it
            }
    }
}