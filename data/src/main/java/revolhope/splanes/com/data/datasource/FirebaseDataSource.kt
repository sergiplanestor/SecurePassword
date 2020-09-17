package revolhope.splanes.com.data.datasource

interface FirebaseDataSource {

    suspend fun login(email: String, pwd: String): Boolean

    suspend fun register(email: String, pwd: String): Boolean
}