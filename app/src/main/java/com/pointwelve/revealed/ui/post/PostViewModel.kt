package com.pointwelve.revealed.ui.post

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.pointwelve.revealed.BuildConfig
import com.pointwelve.revealed.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class PostViewModel @Inject constructor(private val postRepository: PostRepository,
                                        private val account: Auth0,
                                        private val credentialsManager: SecureCredentialsManager
) : ViewModel() {
    private val _postTrigger: MutableLiveData<Unit> = MutableLiveData()
    var posts = _postTrigger.switchMap {
        // Initial launch
        postRepository.loadPost(10, null)
    }
    fun retry() {
        _postTrigger.value = Unit
    }

    fun logout(context: Context) {
        WebAuthProvider.logout(account).withScheme(BuildConfig.APPLICATION_ID)
            .start(context, object: VoidCallback {
                override fun onSuccess(payload: Void?) {
                    credentialsManager.clearCredentials()
                }

                override fun onFailure(error: Auth0Exception?) {
                    Timber.e(error)
                }
            })
    }

}