package com.pointwelve.revealed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.pointwelve.revealed.AppExecutors
import com.pointwelve.revealed.graphql.PostSignupMutation
import com.pointwelve.revealed.graphql.type.PostSignupInput
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apolloClient: ApolloClient
) {
    fun postSignup(input: PostSignupInput): LiveData<Resource<PostSignupMutation.User>> {
        val mediatorLiveData = MutableLiveData<Resource<PostSignupMutation.User>>()
        mediatorLiveData.value = Resource.loading(null)

        val mutation = PostSignupMutation(input)

        apolloClient.mutate(mutation)
            .enqueue(object: ApolloCall.Callback<PostSignupMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    appExecutors.mainThread().execute {
                        mediatorLiveData.value = Resource.error(e.message ?: "Server Error", null)
                    }
                }

                override fun onResponse(response: Response<PostSignupMutation.Data>) {
                    appExecutors.mainThread().execute {
                        if(response.hasErrors()) {
                            mediatorLiveData.value = Resource.error("Server Error", null)
                        } else {
                            mediatorLiveData.value = Resource.success(response.data()?.postSignup?.user)
                        }
                    }
                }
            })

        return mediatorLiveData

    }
}