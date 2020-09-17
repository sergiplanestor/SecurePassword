package revolhope.splanes.com.data.datasourceimpl

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import revolhope.splanes.com.data.datasource.SharedPreferencesDataSource
import revolhope.splanes.com.data.response.LoginDataResponse
import java.lang.Exception
import javax.inject.Inject

class SharedPreferencesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SharedPreferencesDataSource {

    private companion object {
        private const val NAME: String = "secure.password.prefs"

        private const val ENTRY_LOGIN: String = "secure.password.prefs.entry_login"
    }

    private var prefs: SharedPreferences? = null

    private fun getPrefs(): SharedPreferences {
        return prefs ?: context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    override fun fetchLoginData(): LoginDataResponse? =
        try {
            Gson().fromJson(getPrefs().getString(ENTRY_LOGIN, null), LoginDataResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    override fun insertLoginData(loginDataResponse: LoginDataResponse): Boolean =
        try {
            getPrefs().edit().run {
                putString(ENTRY_LOGIN, Gson().toJson(loginDataResponse))
                apply()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}