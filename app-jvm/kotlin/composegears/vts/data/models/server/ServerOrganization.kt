package composegears.vts.data.models.server

import kotlinx.serialization.Serializable

@Serializable
data class ServerOrganization(
    val name: String,
    val description: String = "",
) {
}