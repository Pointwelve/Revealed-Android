package com.pointwelve.revealed.ui.post

import androidx.lifecycle.MutableLiveData
import com.pointwelve.revealed.graphql.fragment.PostDetail

object PostState {
    val postState = MutableLiveData<PostDetail>()
}
