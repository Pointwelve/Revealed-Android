package com.pointwelve.revealed.ui.post

import androidx.lifecycle.ViewModel
import com.pointwelve.revealed.repository.PostRepository
import javax.inject.Inject

class PostViewModel @Inject constructor(private val postRepository: PostRepository
) : ViewModel() {

    var posts = postRepository.loadPost(10, null)
    fun retry() {
        posts = postRepository.loadPost(10, null)
    }

}