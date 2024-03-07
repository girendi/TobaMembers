package com.girendi.tobamembers

import android.app.Application
import com.girendi.tobamembers.core.di.databaseModule
import com.girendi.tobamembers.core.di.networkModule
import com.girendi.tobamembers.core.di.preferencesModule
import com.girendi.tobamembers.core.di.repositoryModule
import com.girendi.tobamembers.core.di.useCaseModule
import com.girendi.tobamembers.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    preferencesModule,
                    databaseModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}