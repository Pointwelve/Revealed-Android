package com.pointwelve.revealed.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pointwelve.revealed.graphql.fragment.PostDetail
import com.pointwelve.revealed.repository.PostRepository
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(private val postRepository: PostRepository
) : ViewModel() {
    fun getPost(id: String): LiveData<Resource<PostDetail>> = postRepository
        .getPost(id)

}