package revolhope.splanes.com.data.datasource

import revolhope.splanes.com.data.response.CryptoObjectResponse
import revolhope.splanes.com.data.response.LoginDataResponse

interface FirebaseDataSource {

    suspend fun login(email: String, pwd: String): Boolean

    suspend fun register(email: String, pwd: String): Boolean

    suspend fun insertUser(email: String, data: CryptoObjectResponse): Boolean

    suspend fun insertData(
        id: String,
        response: CryptoObjectResponse,
        isDirectory: Boolean
    ): Boolean

    suspend fun updateEntry(
        id: String,
        response: CryptoObjectResponse,
        isDirectory: Boolean
    ): Boolean

    suspend fun deleteEntry(
        id: String,
        response: CryptoObjectResponse,
        isDirectory: Boolean
    ): Boolean

    suspend fun fetchData(): List<Pair<CryptoObjectResponse, Boolean>>?

    suspend fun fetchUser(email: String): CryptoObjectResponse?
}