package com.sion.itunes

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.sion.itunes.di.apiModule
import com.sion.itunes.di.dbModule
import com.sion.itunes.di.viewModelModule
import com.sion.itunes.widget.log.DebugLogTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App: Application() {
    companion object {
        const val DB_NAME = "itunes.db"
    }

    @ExperimentalPagingApi
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogTree())
        }

        val module = listOf(
            apiModule,
            dbModule,
            viewModelModule
        )

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(module)
        }
    }
}