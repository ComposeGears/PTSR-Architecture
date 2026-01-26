package composegears.vts.data.models

sealed interface DataOrError<T> {
    data class Data<T>(val data: T) : DataOrError<T>
    data class Error<T>(val error: Throwable) : DataOrError<T>
}