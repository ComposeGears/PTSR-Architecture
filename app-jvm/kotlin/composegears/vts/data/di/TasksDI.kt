package composegears.vts.data.di

import com.composegears.leviathan.Leviathan
import composegears.vts.data.tasks.TasksGetOrganization
import composegears.vts.data.tasks.TasksOrganizationImpl

object TasksDI : Leviathan() {
    val getOrganization by factoryOf<TasksGetOrganization> {
        TasksOrganizationImpl(inject(DataDI.api))
    }
}