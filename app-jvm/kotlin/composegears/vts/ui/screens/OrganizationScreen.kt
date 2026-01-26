package composegears.vts.ui.screens

import androidx.compose.foundation.clickable
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
import composegears.vts.data.models.DataOrError
import composegears.vts.data.models.DomainOrganization
import composegears.vts.data.models.DomainRepository

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val OrganizationScreen by navDestination {
    OrganizationScreenBinding()
}

@OptIn(TiamatExperimentalApi::class)
@Composable
private fun OrganizationScreenBinding() {
    // get tasks
    val organizationData = inject { TasksDI.getOrganization }
    // produce data
    val organizationInfo by produceRetainedState<DataOrError<DomainOrganization>?>(null) {
        value = organizationData.getOrganization()
    }
    // display data
    OrganizationScreenUI(
        organization = organizationInfo,
        organizationsReloadClicked = {
            // TODO
        },
        onRepositoryClick = {
            // TODO nav Repo details
        },
    )
}

@Composable
private fun OrganizationScreenUI(
    organization: DataOrError<DomainOrganization>?,
    organizationsReloadClicked: () -> Unit,
    onRepositoryClick: (DomainRepository) -> Unit,
) {
    Column {
        when (organization) {
            null -> BasicText("Info is loading")
            is DataOrError.Error<*> -> BasicText("Error loading info: ${organization.error.message}")
            is DataOrError.Data<DomainOrganization> -> {
                BasicText("Organization: ${organization.data.name}\nDescription: ${organization.data.description}")
                Spacer(Modifier.height(8.dp))
                BasicText("Repos:")
                organization.data.repositories.onEach { repo ->
                    BasicText(repo.name, modifier = Modifier.clickable { onRepositoryClick(repo) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrganizationScreenPreview() {
    OrganizationScreenUI(
        organization = DataOrError.Data(
            DomainOrganization(
                name = "ComposeGears",
                description = "Description",
                repositories = listOf(
                    DomainRepository("Repo1", "First repo", ""),
                    DomainRepository("Repo2", "Second repo", ""),
                )
            )
        ),
        organizationsReloadClicked = { },
        onRepositoryClick = { }
    )
}

