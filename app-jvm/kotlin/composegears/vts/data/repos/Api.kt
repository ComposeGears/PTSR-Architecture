package composegears.vts.data.repos

import composegears.vts.data.models.NetworkData
import composegears.vts.data.models.OrganizationInfo
import composegears.vts.data.models.RepositoryInfo

interface Api {
    suspend fun getOrganizationInfo(): NetworkData<OrganizationInfo>
    suspend fun getOrganizationRepositories(): NetworkData<Array<RepositoryInfo>>
}

class ApiImpl(
    private val client: HttpClient
) : Api {
    companion object {
        const val ORGANIZATION = "ComposeGears"
        const val BASE_URL = "https://api.github.com"
    }

    override suspend fun getOrganizationInfo(): NetworkData<OrganizationInfo> =
        client.get("$BASE_URL/orgs/$ORGANIZATION")

    override suspend fun getOrganizationRepositories(): NetworkData<Array<RepositoryInfo>> =
        client.get("$BASE_URL/orgs/$ORGANIZATION/repos")
}