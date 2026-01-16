package composegears.vts.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.produceRetainedState
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import composegears.vts.data.di.TasksDI
import composegears.vts.data.models.NetworkData
import composegears.vts.data.models.OrganizationInfo
import composegears.vts.data.models.RepositoryInfo

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val OrganizationScreen by navDestination {
    OrganizationScreenBinding()
}

@OptIn(TiamatExperimentalApi::class)
@Composable
private fun OrganizationScreenBinding() {
    // get tasks
    val organizationData = inject { TasksDI.organizationData }
    // produce data
    val organizationInfo by produceRetainedState<NetworkData<OrganizationInfo>?>(null) {
        value = organizationData.getOrganizationInfo()
    }
    val organizationRepositories by produceRetainedState<NetworkData<Array<RepositoryInfo>>?>(null) {
        value = organizationData.getRepositories()
    }
    // display data
    OrganizationScreenUI(
        organizationInfo = organizationInfo,
        organizationRepositories = organizationRepositories,
        onRepositoryClick = {
            // TODO
        },
    )
}

@Composable
private fun OrganizationScreenUI(
    organizationInfo: NetworkData<OrganizationInfo>?,
    organizationRepositories: NetworkData<Array<RepositoryInfo>>?,
    onRepositoryClick: () -> Unit,
) {
    Column {
        // info
        when (organizationInfo) {
            null -> BasicText("Info is loading")
            is NetworkData.Error<*> -> BasicText("Error loading info: ${organizationInfo.error.message}")
            is NetworkData.Success<OrganizationInfo> -> BasicText(
                "Organization: ${organizationInfo.data.name}\nDescription: ${organizationInfo.data.description}"
            )
        }
        Spacer(Modifier.height(8.dp))
        // repos
        when (organizationRepositories) {
            null -> BasicText("Info is loading")
            is NetworkData.Error<*> -> BasicText("Error loading info: ${organizationRepositories.error.message}")
            is NetworkData.Success<Array<RepositoryInfo>> -> {
                BasicText("Repos:")
                organizationRepositories.data.forEach { repo ->
                    BasicText(repo.name)
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrganizationScreenPreview() {
    OrganizationScreenUI(
        organizationInfo = NetworkData.Success(
            OrganizationInfo("ComposeGears", "Description")
        ),
        organizationRepositories = NetworkData.Success(
            arrayOf(
                RepositoryInfo("Repo1", "First repo", ""),
                RepositoryInfo("Repo2", "Second repo", ""),
            )
        ),
        onRepositoryClick = { }
    )
}

