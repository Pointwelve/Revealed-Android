package com.pointwelve.revealed.di

import android.app.Application
import android.provider.Settings.Global.getString
import com.auth0.android.Auth0
import com.pointwelve.revealed.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideAuth0(app: Application): Auth0 {
        val account = Auth0(app.getString(R.string.com_auth0_client_id), app.getString(R.string.com_auth0_domain))
        account.isOIDCConformant = true
        return account
    }
}
