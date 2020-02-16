package com.pointwelve.revealed.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.pointwelve.revealed.repository.PostRepository
import javax.inject.Inject

class PostViewModel @Inject constructor(private val postRepository: PostRepository
) : ViewModel() {
    fun retry() {

    }

    val posts = postRepository.loadPost(10, null)
    val post = posts.map { it.data?.first() }
}