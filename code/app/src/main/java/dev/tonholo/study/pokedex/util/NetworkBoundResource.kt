package dev.tonholo.study.pokedex.util

import kotlinx.coroutines.flow.*

inline fun <reified TDatabaseType, reified TRequestType> networkBoundResource(
    crossinline query: () -> Flow<TDatabaseType>,
    crossinline fetch: suspend () -> TRequestType,
    crossinline saveFetchResult: suspend (TRequestType) -> Unit,
    crossinline shouldFetch: (TDatabaseType) -> Boolean = { false },
    crossinline onFetchSuccess: (TRequestType) -> Unit = {},
    crossinline onFetchError: (Throwable) -> Unit = {},
): Flow<Resource<TDatabaseType>> = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            val fetchedData = fetch()
            onFetchSuccess(fetchedData)
            saveFetchResult(fetchedData)
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            onFetchError(throwable)
            query().map {
                Resource.Error(
                    throwable = throwable,
                    message = throwable.localizedMessage ?: "Unhandled error",
                    data = it,
                )
            }
        }
    } else {
        query().map {
            Resource.Success(it)
        }
    }

    emitAll(flow)
}
