package revolhope.splanes.com.domain.usecase.user

import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class FetchUserRemoteUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<FetchUserRemoteUseCase.Request, LoginData?>() {

    override suspend fun execute(request: Request): LoginData? =
        userRepository.fetchUserRemote(request.email, request.pwd)

    data class Request(val email: String, val pwd: String)
}