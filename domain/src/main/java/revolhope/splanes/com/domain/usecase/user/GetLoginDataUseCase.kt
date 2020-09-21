package revolhope.splanes.com.domain.usecase.user

import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetLoginDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<GetLoginDataUseCase.Request, LoginData?>() {

    override suspend fun execute(request: Request): LoginData? =
        userRepository.fetchUserLoginData()

    object Request
}