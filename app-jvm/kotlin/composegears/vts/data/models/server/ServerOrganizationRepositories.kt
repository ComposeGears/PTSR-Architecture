package composegears.vts.data.models.server

import kotlinx.serialization.Serializable

typealias ServerRepositories = Array<ServerRepository>

@Serializable
data class ServerRepository(
    val name: String,
    val description: String = "",
    val url: String,
) {
}