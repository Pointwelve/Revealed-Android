package com.pointwelve.revealed.di

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
    abstract fun contributePainFragment(): PostFragment
}
