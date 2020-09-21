package revolhope.splanes.com.domain.usecase.user

import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<RegisterUserUseCase.Request, Boolean?>() {

    override suspend fun execute(request: Request): Boolean? =
        userRepository.registerUser(request.data)

    data class Request(val data: LoginData)
}