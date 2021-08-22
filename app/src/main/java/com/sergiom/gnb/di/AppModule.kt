package com.sergiom.gnb.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sergiom.gnb.data.repository.GNBRepository
import com.sergiom.gnb.data.remote.GNBRemoteDataSource
import com.sergiom.gnb.data.remote.GNBService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://quiet-stone-2094.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideGNBService(retrofit: Retrofit): GNBService = retrofit.create(GNBService::class.java)

    @Singleton
    @Provides
    fun provideGNBRemoteDataSource(eventService: GNBService) = GNBRemoteDataSource(eventService)

    @Singleton
    @Provides
    fun provideGNBRepository(remoteDataSource: GNBRemoteDataSource) = GNBRepository(remoteDataSource)
}