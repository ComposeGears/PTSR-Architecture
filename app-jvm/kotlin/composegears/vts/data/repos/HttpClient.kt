package composegears.vts.data.repos

import composegears.vts.data.models.DataOrError
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.HttpClient as KtorHttpClient

class HttpClient {
    @PublishedApi
    internal val client = KtorHttpClient(CIO) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend inline fun <reified T> get(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): DataOrError<T> = client
        .get(url, block)
        .runCatching { body<T>() }
        .fold(
            onSuccess = { DataOrError.Data(it) },
            onFailure = { DataOrError.Error(it) }
        )
}