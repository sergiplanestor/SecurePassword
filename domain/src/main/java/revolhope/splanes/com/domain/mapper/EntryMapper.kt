package revolhope.splanes.com.domain.mapper

import revolhope.splanes.com.data.response.CryptoObjectResponse
import revolhope.splanes.com.domain.util.cryptographic.CryptographyModel

object EntryMapper {

    fun fromCryptoResponseToModel(response: CryptoObjectResponse): CryptographyModel =
        CryptographyModel(
            raw = response.raw ?: "",
            secret = response.secret ?: "",
            ivRaw = response.ivRaw ?: "",
            ivSecret = response.ivSecret ?: ""
        )

    fun fromCryptoModelToResponse(model: CryptographyModel): CryptoObjectResponse =
        CryptoObjectResponse(
            raw = model.raw,
            secret = model.secret,
            ivRaw = model.ivRaw,
            ivSecret = model.ivSecret
        )
}