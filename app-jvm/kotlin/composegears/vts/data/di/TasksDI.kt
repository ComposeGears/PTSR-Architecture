package composegears.vts.data.di

import com.composegears.leviathan.Leviathan
import composegears.vts.data.tasks.TasksOrganizationData
import composegears.vts.data.tasks.TasksOrganizationDataImpl

object TasksDI : Leviathan() {
    val organizationData by factoryOf<TasksOrganizationData> {
        TasksOrganizationDataImpl(inject(DataDI.api))
    }
}