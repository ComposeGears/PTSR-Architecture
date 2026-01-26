package composegears.vts.data.models

import kotlinx.serialization.Serializable

typealias ServerRepositories = Array<ServerRepository>

@Serializable
data class ServerRepository(
    val name: String,
    val description: String = "",
    val url: String,
) {
}