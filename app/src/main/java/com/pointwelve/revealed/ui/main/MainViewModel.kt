package com.pointwelve.revealed.ui.main

import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.pointwelve.revealed.AppExecutors
import com.pointwelve.revealed.BuildConfig
import com.pointwelve.revealed.R
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val account: Auth0,
                                        private val appExecutors: AppExecutors,
                                        private val credentialsManager: SecureCredentialsManager) : ViewModel() {
    val authLiveData = MutableLiveData<Boolean>()

    init {
        credentialsManager.getCredentials(object :
            BaseCallback<Credentials, CredentialsManagerException> {
            override fun onSuccess(payload: Credentials?) {
                appExecutors.mainThread().execute {
                    authLiveData.value = true
                }
            }

            override fun onFailure(error: CredentialsManagerException?) {
                appExecutors.mainThread().execute {
                    authLiveData.value = false
                }
            }
        })
    }

    fun login(activity: Activity) = viewModelScope.launch {
        //Declare the callback that will receive the result
        val authCallback = object : AuthCallback {
            override fun onFailure(dialog: Dialog) {
                //failed with a dialog
                dialog.show()
            }

            override fun onFailure(exception: AuthenticationException) {
                //failed with an exception
                Timber.e(exception)
            }

            override fun onSuccess(credentials: Credentials) {
                //succeeded!
                credentialsManager.saveCredentials(credentials)

            }
        }

        WebAuthProvider.login(account)
            .withScope("openid profile offline_access email")
            .withScheme(BuildConfig.APPLICATION_ID)
            .withAudience("https://${activity.getString(R.string.com_auth0_domain)}/userinfo")
            .start(activity, authCallback)
    }
}
