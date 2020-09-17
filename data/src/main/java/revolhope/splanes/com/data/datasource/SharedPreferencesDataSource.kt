package revolhope.splanes.com.data.datasource

import revolhope.splanes.com.data.response.LoginDataResponse

interface SharedPreferencesDataSource {

    fun fetchLoginData(): LoginDataResponse?

    fun insertLoginData(loginDataResponse: LoginDataResponse): Boolean

}