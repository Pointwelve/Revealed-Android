package com.pointwelve.revealed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import com.pointwelve.revealed.AppExecutors
import com.pointwelve.revealed.graphql.GetAllConfigsQuery
import com.pointwelve.revealed.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apolloClient: ApolloClient
) {
    fun load(): LiveData<Resource<GetAllConfigsQuery.Data>> {
        val mediatorLiveData = MutableLiveData<Resource<GetAllConfigsQuery.Data>>()
        mediatorLiveData.value = Resource.loading(null)

        val query = GetAllConfigsQuery()

        apolloClient.query(query)
            .httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
            .enqueue(object: ApolloCall.Callback<GetAllConfigsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    appExecutors.mainThread().execute {
                        mediatorLiveData.value = Resource.error(e.message ?: "Server Error", null)
                    }
                }

                override fun onResponse(response: Response<GetAllConfigsQuery.Data>) {
                    appExecutors.mainThread().execute {
                        if(response.hasErrors()) {
                            mediatorLiveData.value = Resource.error("Server Error", null)
                        } else {
                            mediatorLiveData.value = Resource.success(response.data())
                        }
                    }
                }
            })

        return mediatorLiveData

    }
}