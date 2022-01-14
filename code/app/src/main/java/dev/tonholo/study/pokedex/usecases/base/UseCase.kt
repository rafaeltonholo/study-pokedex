package dev.tonholo.study.pokedex.usecases.base

abstract class UseCase<in TRequest: UseCase.Params, out TResponse> {
    abstract suspend operator fun invoke(requestParams: TRequest): TResponse

    interface Params {
        operator fun Params.invoke(
            apply: Params.() -> Params
        ) {
            apply(this)
        }
    }
}
