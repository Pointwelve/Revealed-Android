package com.pointwelve.revealed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.pointwelve.revealed.AppExecutors
import com.pointwelve.revealed.graphql.CreateCommentMutation
import com.pointwelve.revealed.graphql.GetPostCommentQuery
import com.pointwelve.revealed.graphql.fragment.CommentDetail
import com.pointwelve.revealed.graphql.type.CommentInput
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apolloClient: ApolloClient
) {
    fun createComment(input: CommentInput): LiveData<Resource<CommentDetail>> {
        val mediatorLiveData = MutableLiveData<Resource<CommentDetail>>()
        mediatorLiveData.value = Resource.loading(null)

        val mutation = CreateCommentMutation(input)

        apolloClient.mutate(mutation)
            .enqueue(object: ApolloCall.Callback<CreateCommentMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    appExecutors.mainThread().execute {
                        mediatorLiveData.value = Resource.error(e.message ?: "Server Error", null)
                    }
                }

                override fun onResponse(response: Response<CreateCommentMutation.Data>) {
                    appExecutors.mainThread().execute {
                        if(response.hasErrors()) {
                            mediatorLiveData.value = Resource.error("Server Error", null)
                        } else {
                            mediatorLiveData.value = Resource.success(response.data()?.
                                createComment?.fragments?.commentDetail)
                        }
                    }
                }
            })

        return mediatorLiveData

    }

    fun listPostCommets(postId: String, first: Int?, commentFirst: String?): LiveData<Resource<List<CommentDetail>>>  {
        val mediatorLiveData = MutableLiveData<Resource<List<CommentDetail>>>()
        mediatorLiveData.value = Resource.loading(null)

        val query = GetPostCommentQuery(postId, Input.optional(first), Input.optional(commentFirst))

        apolloClient.query(query)
            .enqueue(object: ApolloCall.Callback<GetPostCommentQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    appExecutors.mainThread().execute {
                        mediatorLiveData.value = Resource.error(e.message ?: "Server Error", null)
                    }
                }

                override fun onResponse(response: Response<GetPostCommentQuery.Data>) {
                    appExecutors.mainThread().execute {
                        if(response.hasErrors()) {
                            mediatorLiveData.value = Resource.error("Server Error", null)
                        } else {
                            mediatorLiveData.value = Resource.success(response.data()?.getComments
                                ?.edges?.map{ it.fragments.commentDetail })
                        }
                    }
                }
            })

        return mediatorLiveData
    }
}