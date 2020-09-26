package revolhope.splanes.com.data.datasource

import revolhope.splanes.com.data.response.CryptoObjectResponse

interface FirebaseDataSource {

    suspend fun login(email: String, pwd: String): Boolean

    suspend fun register(email: String, pwd: String): Boolean

    suspend fun insertData(
        id: String,
        response: CryptoObjectResponse,
        isDirectory: Boolean
    ): Boolean

    suspend fun fetchData(): List<Pair<CryptoObjectResponse, Boolean>>?

}