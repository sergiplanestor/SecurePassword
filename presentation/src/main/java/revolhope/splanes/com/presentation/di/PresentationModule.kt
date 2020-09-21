package revolhope.splanes.com.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import revolhope.splanes.com.presentation.util.biometric.BiometricUtils
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun providesCryptographyUtils(): CryptographyUtils {
        return CryptographyUtils()
    }

    @Provides
    @Singleton
    fun providesBiometricUtils(cryptographyUtils: CryptographyUtils): BiometricUtils {
        return BiometricUtils(cryptographyUtils)
    }
}