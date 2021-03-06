package revolhope.splanes.com.domain.usecase

import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import javax.inject.Inject

class InsertLoginDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<InsertLoginDataUseCase.Request, Boolean?>() {

    override suspend fun execute(request: Request): Boolean? =
        userRepository.insertUserLoginData(request.data)

    data class Request(val data: LoginData)
}