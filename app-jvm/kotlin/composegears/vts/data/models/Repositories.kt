package composegears.vts.data.models

import kotlinx.serialization.Serializable

@Serializable
class RepositoryInfo(
    val name: String,
    val description: String = "",
    val url: String,
) {
}