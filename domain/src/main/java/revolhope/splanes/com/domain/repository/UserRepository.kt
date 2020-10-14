package revolhope.splanes.com.domain.repository

import revolhope.splanes.com.domain.model.LoginData

interface UserRepository {

    suspend fun fetchUserLoginData(): LoginData?

    suspend fun insertUserLoginData(loginData: LoginData): Boolean?

    suspend fun registerUser(loginData: LoginData): Boolean

    suspend fun doUserLogin(loginData: LoginData): Boolean

    suspend fun fetchUserRemote(email: String, pwd: String): LoginData?
}