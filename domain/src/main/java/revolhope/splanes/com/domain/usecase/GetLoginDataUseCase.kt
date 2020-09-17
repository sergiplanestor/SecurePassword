package revolhope.splanes.com.domain.usecase

import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import javax.inject.Inject

class GetLoginDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<GetLoginDataUseCase.Request, LoginData?>() {

    override suspend fun execute(request: Request): LoginData? =
        userRepository.fetchUserLoginData()

    object Request
}