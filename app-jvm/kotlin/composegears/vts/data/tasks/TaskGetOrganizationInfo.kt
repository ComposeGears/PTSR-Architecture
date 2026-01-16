package composegears.vts.data.tasks

import composegears.vts.data.models.NetworkData
import composegears.vts.data.models.OrganizationInfo
import composegears.vts.data.models.RepositoryInfo
import composegears.vts.data.repos.Api

interface TasksOrganizationData {
    suspend fun getOrganizationInfo(): NetworkData<OrganizationInfo>
    suspend fun getRepositories(): NetworkData<Array<RepositoryInfo>>
}

class TasksOrganizationDataImpl(
    private val api: Api
) : TasksOrganizationData {
    override suspend fun getOrganizationInfo(): NetworkData<OrganizationInfo> =
        api.getOrganizationInfo()

    override suspend fun getRepositories(): NetworkData<Array<RepositoryInfo>> =
        api.getOrganizationRepositories()
}