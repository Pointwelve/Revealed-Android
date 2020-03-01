package com.pointwelve.revealed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pointwelve.revealed.RevealedViewModelFactory
import com.pointwelve.revealed.ui.createPost.CreatePostViewModel
import com.pointwelve.revealed.ui.getStarted.GetStartedViewModel
import com.pointwelve.revealed.ui.main.MainViewModel
import com.pointwelve.revealed.ui.post.PostViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun bindPostViewModel(postViewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePostViewModel::class)
    abstract fun bindCreatePostViewModel(createPostViewModel: CreatePostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GetStartedViewModel::class)
    abstract fun bindGetStartedViewModel(getStartedViewModel: GetStartedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: RevealedViewModelFactory): ViewModelProvider.Factory
}
