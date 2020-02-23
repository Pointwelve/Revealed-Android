package com.pointwelve.revealed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.pointwelve.revealed.AppExecutors
import com.pointwelve.revealed.graphql.CreatePostMutation
import com.pointwelve.revealed.graphql.GetAllPostQuery
import com.pointwelve.revealed.graphql.fragment.PostDetail
import com.pointwelve.revealed.graphql.type.PostInput
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apolloClient: ApolloClient
) {
    fun loadPost(first: Int?, commentFirst: String?): LiveData<Resource<List<PostDetail>>> {
        val mediatorLiveData = MutableLiveData<Resource<List<PostDetail>>>()
        mediatorLiveData.value = Resource.loading(null)

        val query = GetAllPostQuery(Input.optional(first), Input.optional(commentFirst))

        apolloClient.query(query)
            .enqueue(object: ApolloCall.Callback<GetAllPostQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    appExecutors.mainThread().execute {
                        mediatorLiveData.value = Resource.error(e.message ?: "Server Error", null)
                    }
                }

                override fun onResponse(response: Response<GetAllPostQuery.Data>) {
                    appExecutors.mainThread().execute {
                        if(response.hasErrors()) {
                            mediatorLiveData.value = Resource.error("Server Error", null)
                        } else {
                            mediatorLiveData.value = Resource.success(response.data()
                                ?.getAllPosts?.edges?.map { it.fragments.postDetail })
                        }
                    }
                }
            })

        return mediatorLiveData

    }

    fun createPost(input: PostInput): LiveData<Resource<PostDetail>> {
        val mediatorLiveData = MutableLiveData<Resource<PostDetail>>()
        mediatorLiveData.value = Resource.loading(null)

        val mutation = CreatePostMutation(input)

        apolloClient.mutate(mutation)
            .enqueue(object: ApolloCall.Callback<CreatePostMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    appExecutors.mainThread().execute {
                        mediatorLiveData.value = Resource.error(e.message ?: "Server Error", null)
                    }
                }

                override fun onResponse(response: Response<CreatePostMutation.Data>) {
                    appExecutors.mainThread().execute {
                        if(response.hasErrors()) {
                            mediatorLiveData.value = Resource.error("Server Error", null)
                        } else {
                            mediatorLiveData.value = Resource.success(response.data()?.createPost?.fragments?.postDetail)
                        }
                    }
                }
            })

        return mediatorLiveData
    }
}