package com.pointwelve.revealed.ui.create_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pointwelve.revealed.graphql.GetAllConfigsQuery
import com.pointwelve.revealed.repository.ConfigRepository
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject


class CreatePostViewModel @Inject constructor(private val configRepository: ConfigRepository) : ViewModel() {
    val configs: LiveData<Resource<GetAllConfigsQuery.Data>> = configRepository.load()
}
