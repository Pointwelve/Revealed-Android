package com.pointwelve.revealed.di

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.pointwelve.revealed.BuildConfig
import com.pointwelve.revealed.R
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {
    private companion object {
        const val CONNECT_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 30L
        const val READ_TIMEOUT = 30L
        const val CACHE_SIZE = 10 * 1024 * 1024 // 10MB
    }

    @Singleton
    @Provides
    fun provideAuth0(app: Application): Auth0 {
        val account = Auth0(app)
        account.isOIDCConformant = true
        return account
    }

    @Singleton
    @Provides
    fun provideSecurityCredentialsManager(app: Application, account: Auth0): SecureCredentialsManager {
        return SecureCredentialsManager(
            app,
            AuthenticationAPIClient(account),
            SharedPreferencesStorage(app)
        )
    }

    @Singleton
    @Provides
    fun provideOkHttp(app: Application, secureCredentialsManager: SecureCredentialsManager): OkHttpClient {
        val client = OkHttpClient.Builder()
            .cache(Cache(app.cacheDir, CACHE_SIZE.toLong()))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            )
            .addInterceptor(AuthInterceptor(secureCredentialsManager))

        return client.build()
    }

    @Singleton
    @Provides
    fun provideApolloClient(app: Application, okHttpClient: OkHttpClient): ApolloClient {

        return ApolloClient.builder()
            .serverUrl(app.getString(R.string.graphql_host))
            .okHttpClient(okHttpClient)
            .build()
    }
}
