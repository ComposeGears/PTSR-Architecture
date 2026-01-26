package composegears.vts.data.tasks

import composegears.vts.data.models.DataOrError
import composegears.vts.data.models.DomainOrganization
import composegears.vts.data.models.DomainRepository
import composegears.vts.data.repos.Api
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface TasksGetOrganization {
    suspend fun getOrganization(): DataOrError<DomainOrganization>
}

class TasksOrganizationImpl(
    private val api: Api
) : TasksGetOrganization {
    override suspend fun getOrganization(): DataOrError<DomainOrganization> = coroutineScope {
        val organizationInfo = async { api.getOrganization() }
        val organizationRepository = async { api.getOrganizationRepositories() }
        val orgInfoResult = organizationInfo.await()
        val orgReposResult = organizationRepository.await()
        when {
            orgInfoResult is DataOrError.Data && orgReposResult is DataOrError.Data -> {
                val domainOrganization = DomainOrganization(
                    name = orgInfoResult.data.name,
                    description = orgInfoResult.data.description,
                    repositories = orgReposResult.data.map {
                        DomainRepository(
                            name = it.name,
                            description = it.description,
                            url = it.url
                        )
                    }
                )
                DataOrError.Data(domainOrganization)
            }
            orgInfoResult is DataOrError.Error -> {
                DataOrError.Error(orgInfoResult.error)
            }
            orgReposResult is DataOrError.Error -> {
                DataOrError.Error(orgReposResult.error)
            }
            else -> {
                // should never happen
                DataOrError.Error(Exception("Unknown error occurred"))
            }
        }
    }
}