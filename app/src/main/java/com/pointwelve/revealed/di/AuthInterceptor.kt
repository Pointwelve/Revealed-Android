package com.pointwelve.revealed.di

import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class AuthInterceptor constructor(
    credentialsManager: SecureCredentialsManager,
    private var authToken: String? = null
) : Interceptor {
    init {
        credentialsManager.getCredentials(object :
            BaseCallback<Credentials, CredentialsManagerException> {
            override fun onSuccess(payload: Credentials?) {
                authToken = payload?.accessToken
            }

            override fun onFailure(error: CredentialsManagerException?) {
                Timber.e(error)
            }
        })
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        authToken?.let {
            builder.addHeader("Authorization", it)
        }
        return chain.proceed( builder.build())
    }

}