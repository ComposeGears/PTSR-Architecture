package composegears.vts.data.models

import kotlinx.serialization.Serializable

@Serializable
class OrganizationInfo(
    val name: String,
    val description: String = "",
) {
}