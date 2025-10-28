package com.example.sombriyakotlin.data.di

import com.example.sombriyakotlin.BuildConfig
import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.StationApi
import com.example.sombriyakotlin.data.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://sombri-ya-back-4def07fa1804.herokuapp.com/"

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides @Singleton
    fun provideRentalApi(retrofit: Retrofit): RentalApi =
        retrofit.create(RentalApi::class.java)

    @Provides @Singleton
    fun provideStationApi(retrofit: Retrofit): StationApi =
        retrofit.create(StationApi::class.java)


    @Provides @Singleton
    @Named("OWM_API_KEY")
    fun provideOwmApiKey(): String = BuildConfig.OWM_API_URL
}
