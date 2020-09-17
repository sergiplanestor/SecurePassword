package revolhope.splanes.com.domain.mapper

import revolhope.splanes.com.data.response.LoginDataResponse
import revolhope.splanes.com.domain.model.LoginData

object UserMapper {

    fun fromLoginDataResponseToModel(response: LoginDataResponse) : LoginData =
        LoginData(
            email = response.email ?: "",
            pwd = response.pwd ?: "",
            pattern = response.pattern ?: "",
            defaultAuthMethod = response.defaultAuthMethod,
            createdOn = response.createdOn ?: 0L,
            lastAccessOn = response.lastAccessOn ?: 0L
        )

    fun fromLoginDataModelToResponse(model: LoginData): LoginDataResponse =
        LoginDataResponse(
            email = model.email,
            pwd = model.pwd,
            pattern = model.pattern,
            defaultAuthMethod = model.defaultAuthMethod,
            createdOn = model.createdOn,
            lastAccessOn = model.lastAccessOn
        )
}