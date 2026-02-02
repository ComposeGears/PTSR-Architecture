package composegears.vts.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.TiamatExperimentalApi
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.destinations.InstallIn
import composegears.vts.AppGraph
import composegears.vts.data.models.domian.DomainRepository
import composegears.vts.ui.res.IconArrowBack
import composegears.vts.ui.components.*

@OptIn(TiamatExperimentalApi::class)
@InstallIn(AppGraph::class)
val RepositoryScreen by navDestination<DomainRepository> {
    val repository = navArgs()
    val navController = navController()
    RepositoryScreenUI(
        repository = repository,
        onBackClick = { navController.back() },
    )
}

@Composable
private fun RepositoryScreenUI(
    repository: DomainRepository,
    onBackClick: () -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Row(
                modifier = Modifier.height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    imageVector = IconArrowBack,
                    modifier = Modifier.fillMaxHeight(),
                    onClick = onBackClick,
                )
                Column(
                    modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextLabel(repository.name)
                    if (repository.description.isNotBlank()) {
                        TextHint(
                            repository.description,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Divider(Modifier.fillMaxWidth().height(1.dp))
            Spacer(Modifier.height(8.dp))
            // todo branch selector
            // todo files list
            // todo file preview
        }
    }
}

@Preview
@Composable
private fun OrganizationScreenPreview() {
    RepositoryScreenUI(
        repository = DomainRepository(
            name = "Some Repository",
            description = "Repository description",
            url = "url"
        ),
        onBackClick = {}
    )
}