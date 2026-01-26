package composegears.vts.data.repos

import composegears.vts.data.models.DataOrError
import composegears.vts.data.models.ServerOrganization
import composegears.vts.data.models.ServerRepositories

interface Api {
    suspend fun getOrganization(): DataOrError<ServerOrganization>
    suspend fun getOrganizationRepositories(): DataOrError<ServerRepositories>
}

class ApiImpl(
    private val client: HttpClient
) : Api {
    companion object {
        const val ORGANIZATION = "ComposeGears"
        const val BASE_URL = "https://api.github.com"
    }

    override suspend fun getOrganization(): DataOrError<ServerOrganization> =
        client.get("$BASE_URL/orgs/$ORGANIZATION")

    override suspend fun getOrganizationRepositories(): DataOrError<ServerRepositories> =
        client.get("$BASE_URL/orgs/$ORGANIZATION/repos")
}