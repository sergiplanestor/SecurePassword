package revolhope.splanes.com.data.datasourceimpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import revolhope.splanes.com.data.datasource.FirebaseDataSource
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSourceImpl @Inject constructor() : FirebaseDataSource {

    companion object {
        const val REF_USR = "db/content/serie"
    }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    override suspend fun login(email: String, pwd: String): Boolean =
        suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { cont.resume(it.isSuccessful) }
        }

    override suspend fun register(email: String, pwd: String): Boolean =
        suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { cont.resume(it.isSuccessful) }
        }


}