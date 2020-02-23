package com.pointwelve.revealed.ui.create_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pointwelve.revealed.graphql.GetAllConfigsQuery
import com.pointwelve.revealed.graphql.fragment.PostDetail
import com.pointwelve.revealed.graphql.type.PostInput
import com.pointwelve.revealed.repository.ConfigRepository
import com.pointwelve.revealed.repository.PostRepository
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject


class CreatePostViewModel @Inject constructor(configRepository: ConfigRepository,
                                              private val postRepository: PostRepository) : ViewModel() {

    private val _postInput = MutableLiveData<PostInput>()
    val configs: LiveData<Resource<GetAllConfigsQuery.Data>> = configRepository.load()

    val createPostResults = _postInput.switchMap { postRepository.createPost(it) }

    fun createPost(subject: String, content: String, topicId: String, tagIds: List<String>) {
        val postInput = PostInput(subject, content, topicId, tagIds)
        _postInput.value = postInput
    }
}
