package com.pointwelve.revealed.ui.getStarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pointwelve.revealed.graphql.type.DeviceInput
import com.pointwelve.revealed.graphql.type.Platform
import com.pointwelve.revealed.graphql.type.PostSignupInput
import com.pointwelve.revealed.repository.AuthRepository
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject


class GetStartedViewModel @Inject constructor(private val authRepository: AuthRepository
) : ViewModel() {

    private val _username = MutableLiveData<Resource<com.pointwelve.revealed.graphql.PostSignupMutation.User>>()
    val username: LiveData<Resource<com.pointwelve.revealed.graphql.PostSignupMutation.User>>
        get() = _username

    fun signupUser(username: String) = authRepository
        .postSignup(PostSignupInput(username, DeviceInput("", Platform.ANDROID)))
        .let { _username.value = it.value }

}