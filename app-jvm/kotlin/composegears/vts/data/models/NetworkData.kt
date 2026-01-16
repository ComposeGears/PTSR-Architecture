package composegears.vts.data.models

sealed interface NetworkData<T> {
    data class Success<T>(val data: T) : NetworkData<T>
    data class Error<T>(val error: Throwable) : NetworkData<T>
}