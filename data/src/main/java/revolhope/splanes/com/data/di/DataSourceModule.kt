package revolhope.splanes.com.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import revolhope.splanes.com.data.datasource.FirebaseDataSource
import revolhope.splanes.com.data.datasource.SharedPreferencesDataSource
import revolhope.splanes.com.data.datasourceimpl.FirebaseDataSourceImpl
import revolhope.splanes.com.data.datasourceimpl.SharedPreferencesDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindSharedPreferencesDataSource(
        sharedPreferencesDataSourceImpl: SharedPreferencesDataSourceImpl
    ): SharedPreferencesDataSource

    @Binds
    @Singleton
    abstract fun bindFirebaseDataSource(
        firebaseDataSourceImpl: FirebaseDataSourceImpl
    ): FirebaseDataSource
}