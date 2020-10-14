package revolhope.splanes.com.domain.repositoryimpl

import com.google.gson.Gson
import revolhope.splanes.com.data.datasource.FirebaseDataSource
import revolhope.splanes.com.data.datasource.SharedPreferencesDataSource
import revolhope.splanes.com.domain.mapper.EntryMapper
import revolhope.splanes.com.domain.mapper.UserMapper
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryKeyModel
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.repository.UserRepository
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils
import revolhope.splanes.com.domain.util.sha256
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val cryptographyUtils: CryptographyUtils
) : UserRepository {

    override suspend fun fetchUserLoginData(): LoginData? =
        sharedPreferencesDataSource.fetchLoginData()?.let(UserMapper::fromLoginDataResponseToModel)

    override suspend fun insertUserLoginData(loginData: LoginData): Boolean? =
        sharedPreferencesDataSource.insertLoginData(
            loginData.let(UserMapper::fromLoginDataModelToResponse)
        )

    override suspend fun registerUser(loginData: LoginData): Boolean {
        val registerResponse = firebaseDataSource.register(loginData.email, loginData.pwd)
        val insertResponse = firebaseDataSource.insertUser(
            email = sha256(loginData.email),
            data = cryptographyUtils.encrypt(Gson().toJson(loginData)).let(EntryMapper::fromCryptoModelToResponse)
        )
        return registerResponse && insertResponse
    }

    override suspend fun doUserLogin(loginData: LoginData): Boolean =
        firebaseDataSource.login(loginData.email, loginData.pwd)

    override suspend fun fetchUserRemote(email: String, pwd: String): LoginData? {
        val loginResponse = firebaseDataSource.login(email, sha256(pwd))
        return if (loginResponse) {
            firebaseDataSource.fetchUser(sha256(email))?.let {
                cryptographyUtils.decrypt(it.let(EntryMapper::fromCryptoResponseToModel)).run {
                    Gson().fromJson(
                        this,
                        LoginData::class.java
                    )
                }
            }
        } else {
            null
        }
    }
}