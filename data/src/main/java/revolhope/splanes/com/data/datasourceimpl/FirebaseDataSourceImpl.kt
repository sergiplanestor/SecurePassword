package revolhope.splanes.com.data.datasourceimpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import revolhope.splanes.com.data.datasource.FirebaseDataSource
import revolhope.splanes.com.data.response.CryptoObjectResponse
import revolhope.splanes.com.data.response.EntryResponse
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSourceImpl @Inject constructor() : FirebaseDataSource {

    companion object {
        const val ENTRY = "entry"
        const val DIRECTORY = "dir"
    }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    private fun ref(path: String): String? = auth.currentUser?.uid?.let { "${it}/${path}" }

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

    override suspend fun insertData(
        id: String,
        response: CryptoObjectResponse,
        isDirectory: Boolean
    ): Boolean =
        suspendCoroutine { cont ->
            ref(if (isDirectory) DIRECTORY else ENTRY)?.let { ref ->
                database.getReference("${ref}/${id}").setValue(Gson().toJson(response))
                    .addOnCompleteListener {
                        cont.resume(it.isSuccessful)
                    }
            } ?: cont.resume(false)
        }

    override suspend fun fetchData(): List<Pair<CryptoObjectResponse, Boolean>>? {
        val entries = fetchEntries().map { it to false }
        val directories = fetchDirectories().map { it to true}
        return entries.toMutableList().apply { addAll(directories) }
    }

    private suspend fun fetchEntries(): List<CryptoObjectResponse> =
        suspendCoroutine { cont ->
            ref(ENTRY)?.let { ref ->
                database.getReference(ref)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) = cont.resume(
                            snapshot.children.map {
                                Gson().fromJson(
                                    it.value as? String,
                                    CryptoObjectResponse::class.java
                                )
                            }
                        )

                        override fun onCancelled(error: DatabaseError) = cont.resume(emptyList())
                    })
            } ?: cont.resume(emptyList())
        }

    private suspend fun fetchDirectories(): List<CryptoObjectResponse> =
        suspendCoroutine { cont ->
            ref(DIRECTORY)?.let { ref ->
                database.getReference(ref)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) = cont.resume(
                            snapshot.children.map {
                                Gson().fromJson(
                                    it.value as? String,
                                    CryptoObjectResponse::class.java
                                )
                            }
                        )

                        override fun onCancelled(error: DatabaseError) = cont.resume(emptyList())
                    })
            } ?: cont.resume(emptyList())
        }
}