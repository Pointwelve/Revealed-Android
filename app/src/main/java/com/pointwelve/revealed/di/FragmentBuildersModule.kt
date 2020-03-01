package com.pointwelve.revealed.di

import com.pointwelve.revealed.ui.createPost.CreatePostFragment
import com.pointwelve.revealed.ui.getStarted.GetStartedFragment
import com.pointwelve.revealed.ui.main.MainFragment
import com.pointwelve.revealed.ui.post.PostFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributePostFragment(): PostFragment

    @ContributesAndroidInjector
    abstract fun contributeCreatePostFragment(): CreatePostFragment

    @ContributesAndroidInjector
    abstract fun contributeGetStartedFragment(): GetStartedFragment
}
