package com.pointwelve.revealed.di

import android.app.Application
import com.auth0.android.Auth0
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideAuth0(app: Application): Auth0 {
        val account = Auth0("{YOUR_CLIENT_ID}", "{YOUR_DOMAIN}")
        account.isOIDCConformant = true
        return account
    }
}
