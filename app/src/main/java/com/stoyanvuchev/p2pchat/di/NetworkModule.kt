package com.stoyanvuchev.p2pchat.di

import android.content.Context
import com.stoyanvuchev.p2pchat.network.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        @ApplicationContext context: Context
    ): ChatRepository {
        return ChatRepository(context)
    }

}