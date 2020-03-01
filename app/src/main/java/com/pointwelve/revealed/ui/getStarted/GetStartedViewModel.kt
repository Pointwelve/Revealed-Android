package com.pointwelve.revealed.ui.getStarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pointwelve.revealed.graphql.fragment.UserDetail
import com.pointwelve.revealed.graphql.type.DeviceInput
import com.pointwelve.revealed.graphql.type.Platform
import com.pointwelve.revealed.graphql.type.PostSignupInput
import com.pointwelve.revealed.repository.AuthRepository
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject

class GetStartedViewModel @Inject constructor(private val authRepository: AuthRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val user = _username.switchMap { authRepository
        .postSignup(PostSignupInput(it, DeviceInput("", Platform.ANDROID))) }

    fun signupUser(username: String) {
        _username.value = username
    }

}