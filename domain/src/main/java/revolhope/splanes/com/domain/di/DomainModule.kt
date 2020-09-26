package revolhope.splanes.com.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.repository.UserRepository
import revolhope.splanes.com.domain.repositoryimpl.EntryRepositoryImpl
import revolhope.splanes.com.domain.repositoryimpl.UserRepositoryImpl
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils

@Module
@InstallIn(ApplicationComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindEntryRepository(
        entryRepositoryImpl: EntryRepositoryImpl,
    ): EntryRepository
}