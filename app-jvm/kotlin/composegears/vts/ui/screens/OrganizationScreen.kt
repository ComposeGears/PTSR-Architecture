package composegears.vts.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.produceRetainedState
import com.composegears.tiamat.destinations.InstallIn
import com.composegears.tiamat.navigation.NavController
import composegears.vts.AppGraph
import composegears.vts.data.di.TasksDI
import composegears.vts.data.models.DataOrError
import composegears.vts.data.models.domian.DomainOrganization
import composegears.vts.data.models.domian.DomainRepository
import composegears.vts.ui.res.IconArrowForwardSmall
import composegears.vts.ui.components.*

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val OrganizationScreen by navDestination {
    OrganizationScreenBinding(navController())
}

@OptIn(TiamatExperimentalApi::class)
@Composable
private fun OrganizationScreenBinding(
    navController: NavController
) {
    // get tasks
    val organizationData = inject { TasksDI.getOrganization }
    val reloadTrigger = retainTrigger()
    val organizationInfo by produceRetainedState<DataOrError<DomainOrganization>?>(null, reloadTrigger) {
        value = organizationData.getOrganization()
    }
    // display data
    OrganizationScreenUI(
        organization = organizationInfo,
        organizationsReloadClicked = reloadTrigger,
        onRepositoryClick = { navController.navigate(RepositoryScreen, it) },
    )
}

@Composable
private fun OrganizationScreenUI(
    organization: DataOrError<DomainOrganization>?,
    organizationsReloadClicked: () -> Unit,
    onRepositoryClick: (DomainRepository) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))
            when (organization) {
                null -> {
                    Loader()
                }
                is DataOrError.Error<*> -> {
                    BasicText("Error loading info: ${organization.error.message}")
                    Button(onClick = organizationsReloadClicked) {
                        TextButton("Reload")
                    }
                }
                is DataOrError.Data<DomainOrganization> -> {
                    TextLabel(organization.data.name)
                    Spacer(Modifier.height(8.dp))
                    TextLabel(organization.data.description)
                    Spacer(Modifier.height(8.dp))
                    Divider(Modifier.fillMaxWidth().height(1.dp))
                    Spacer(Modifier.height(8.dp))
                    TextLabel("Repositories:")
                    Spacer(Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        organization.data.repositories.onEach {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onRepositoryClick(it) }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TextButton(it.name, Modifier.weight(1f))
                                    Spacer(Modifier.width(8.dp))
                                    Image(imageVector = IconArrowForwardSmall, contentDescription = null)
                                }
                            }
                        }
                    }
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
                    DomainRepository("Some other Repo2", "Second repo", ""),
                )
            )
        ),
        organizationsReloadClicked = { },
        onRepositoryClick = { }
    )
}