package com.example.sombriyakotlin.data.di

import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.StationApi
import com.example.sombriyakotlin.data.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://sombri-ya-back-4def07fa1804.herokuapp.com/"
    // private const val BASE_URL = "http://localhost:3000/"
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRentalApi(retrofit: Retrofit): RentalApi {
        return retrofit.create(RentalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStationApi(retrofit: Retrofit): StationApi {
        return retrofit.create(StationApi::class.java)
    }

}