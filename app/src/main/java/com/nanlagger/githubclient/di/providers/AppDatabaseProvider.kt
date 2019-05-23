package com.nanlagger.githubclient.di.providers

import android.app.Application
import androidx.room.Room
import com.nanlagger.githubclient.data.database.AppDatabase
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseProvider @Inject constructor(
    private val application: Application
) : Provider<AppDatabase> {
    override fun get(): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "github.db").build()
    }
}