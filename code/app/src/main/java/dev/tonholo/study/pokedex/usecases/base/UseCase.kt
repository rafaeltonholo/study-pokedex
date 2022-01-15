package dev.tonholo.study.pokedex.usecases.base

abstract class UseCase<in TRequest, out TResponse> {
    abstract suspend operator fun invoke(requestParams: TRequest): TResponse
}
