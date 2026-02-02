package composegears.vts.data.models.domian

class DomainOrganization(
    val name: String,
    val description: String,
    val repositories: List<DomainRepository>
)