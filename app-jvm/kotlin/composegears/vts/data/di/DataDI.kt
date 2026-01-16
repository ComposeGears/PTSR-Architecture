package composegears.vts.data.di

import com.composegears.leviathan.Leviathan
import composegears.vts.data.repos.Api
import composegears.vts.data.repos.ApiImpl
import composegears.vts.data.repos.HttpClient

object DataDI : Leviathan() {
    val httpClient by instanceOf(keepAlive = true) { HttpClient() }
    val api by instanceOf<Api>(keepAlive = true) {
        ApiImpl(client = inject(httpClient))
    }
}