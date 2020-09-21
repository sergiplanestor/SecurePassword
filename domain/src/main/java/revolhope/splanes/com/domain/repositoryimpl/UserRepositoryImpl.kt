package revolhope.splanes.com.domain.repositoryimpl

import revolhope.splanes.com.data.datasource.FirebaseDataSource
import revolhope.splanes.com.data.datasource.SharedPreferencesDataSource
import revolhope.splanes.com.domain.mapper.UserMapper
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : UserRepository {

    override suspend fun fetchUserLoginData(): LoginData? =
        sharedPreferencesDataSource.fetchLoginData()?.let(UserMapper::fromLoginDataResponseToModel)

    override suspend fun insertUserLoginData(loginData: LoginData): Boolean? =
        sharedPreferencesDataSource.insertLoginData(
            loginData.let(UserMapper::fromLoginDataModelToResponse)
        )

    override suspend fun registerUser(loginData: LoginData): Boolean =
        firebaseDataSource.register(loginData.email, loginData.pwd)

    override suspend fun doUserLogin(loginData: LoginData): Boolean =
        firebaseDataSource.login(loginData.email, loginData.pwd)
}