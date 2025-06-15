package  com.sns.homeconnect_v2.core.di

import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.api.EcomApiService
import com.sns.homeconnect_v2.data.remote.api.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("HomeConnectRetrofit")
    fun provideHomeConnectRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://iothomeconnectapiv2-production.up.railway.app/api/") // Thay đổi thành localhost/IP thật
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("HomeConnectEcomUrl")
    fun provideHomeConnectEcomUrl(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.227.180:8081/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("WeatherRetrofit")
    fun provideWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("HomeConnectRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("HomeConnectEcomUrl") // BẮT BUỘC PHẢI CÓ TRÊN RETURN VALUE
    fun provideEcomApiService(
        @Named("HomeConnectEcomUrl") retrofit: Retrofit
    ): EcomApiService {
        return retrofit.create(EcomApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(@Named("WeatherRetrofit") retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

}