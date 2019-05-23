package com.nanlagger.githubclient

import android.app.Application
import com.nanlagger.githubclient.di.Scopes
import com.nanlagger.githubclient.di.module.*
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration

class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initToothpick()
        Toothpick.openScope(Scopes.APP_SCOPE)
            .installModules(NavigationModule(), RepositoryModule(), NetworkModule(), ProjectModule(this), DatabaseModule())
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction())
        }
    }


}