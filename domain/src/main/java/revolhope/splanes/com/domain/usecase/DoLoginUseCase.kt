package revolhope.splanes.com.domain.usecase

import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import javax.inject.Inject

class DoLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<DoLoginUseCase.Request, Boolean>() {

    override suspend fun execute(request: Request): Boolean =
        userRepository.doUserLogin(request.loginData)

    data class Request(val loginData: LoginData)
}