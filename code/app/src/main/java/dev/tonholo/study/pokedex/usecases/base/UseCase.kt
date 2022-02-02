package dev.tonholo.study.pokedex.usecases.base

import kotlinx.coroutines.flow.Flow

abstract class UseCase<in TRequest, out TResponse> {
    abstract suspend operator fun invoke(requestParams: TRequest): TResponse
}

abstract class UseCaseFlowable<in TRequest, out TResponse> {
    abstract operator fun invoke(requestParams: TRequest): Flow<TResponse>
}
