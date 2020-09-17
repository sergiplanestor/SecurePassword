package revolhope.splanes.com.domain.usecase

import revolhope.splanes.com.domain.network.ResponseState

abstract class BaseUseCase<REQ, RES> {

    suspend operator fun invoke(req: REQ): ResponseState<RES> =
        try {
            execute(req)?.let {
                ResponseState.Success(data = it)
            } ?: ResponseState.Error(message = null, throwable = Exception())
        } catch (e: Exception) {
            ResponseState.Error(message = null, throwable = e)
        }

    abstract suspend fun execute(request: REQ): RES?
}