package com.onegravity.tichucount

import android.content.Context
import androidx.room.Room
import com.funnydevs.hilt_conductor.ControllerComponent
import com.funnydevs.hilt_conductor.annotations.ControllerScoped
import com.onegravity.tichucount.db.TichuDatabase
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.LoggerImpl
import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TichuDatabase {
        return Room
            .databaseBuilder(context, TichuDatabase::class.java, "tichu-db")
            .build()
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger = LoggerImpl(true)

}

@Module
@InstallIn(ControllerComponent::class)
object ControllerModule {

    @Provides
    @ControllerScoped
    fun provideContext(@ApplicationContext context: Context): Context = context

}
