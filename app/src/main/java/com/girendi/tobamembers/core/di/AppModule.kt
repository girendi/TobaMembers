package com.girendi.tobamembers.core.di

import androidx.room.Room
import com.girendi.tobamembers.BuildConfig
import com.girendi.tobamembers.core.data.source.local.LocalDataSource
import com.girendi.tobamembers.core.data.source.local.room.TobaMembersDatabase
import com.girendi.tobamembers.core.data.source.remote.api.ApiService
import com.girendi.tobamembers.core.data.source.repository.PhotoRepositoryImpl
import com.girendi.tobamembers.core.data.source.repository.SharedPreferencesSessionManagerRepositoryImpl
import com.girendi.tobamembers.core.data.source.repository.UserRepositoryImpl
import com.girendi.tobamembers.core.domain.repository.PhotoRepository
import com.girendi.tobamembers.core.domain.repository.SessionManagerRepository
import com.girendi.tobamembers.core.domain.repository.UserRepository
import com.girendi.tobamembers.core.domain.usecase.AdminUseCase
import com.girendi.tobamembers.core.domain.usecase.MainUseCase
import com.girendi.tobamembers.core.domain.usecase.LoginUserUseCase
import com.girendi.tobamembers.core.domain.usecase.RegisterUserUseCase
import com.girendi.tobamembers.core.domain.usecase.SplashUseCase
import com.girendi.tobamembers.persentation.admin.AdminViewModel
import com.girendi.tobamembers.persentation.login.LoginViewModel
import com.girendi.tobamembers.persentation.main.MainViewModel
import com.girendi.tobamembers.persentation.register.RegisterViewModel
import com.girendi.tobamembers.persentation.splash.SplashViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .cache(null)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .build()
                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

val preferencesModule = module {
    single<SessionManagerRepository> { SharedPreferencesSessionManagerRepositoryImpl(androidContext()) }
}

val databaseModule = module {
    factory { get<TobaMembersDatabase>().userEntityDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            TobaMembersDatabase::class.java, "TobaMembers.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { LocalDataSource(userEntityDao = get()) }
}

val repositoryModule = module {
    single<PhotoRepository> { PhotoRepositoryImpl(apiService = get()) }
    single<UserRepository> { UserRepositoryImpl(localDataSource = get()) }
}

val useCaseModule = module {
    factory { MainUseCase(
        photoRepository = get(),
        sessionManagerRepository = get()
    ) }
    factory { LoginUserUseCase(
        userRepository = get(),
        sessionManagerRepository = get()
    ) }
    factory { AdminUseCase(
        userRepository = get(),
        sessionManagerRepository = get()
    ) }
    factory { SplashUseCase(sessionManagerRepository = get()) }
    factory { RegisterUserUseCase(userRepository = get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(mainUseCase = get()) }
    viewModel { LoginViewModel(loginUserUseCase = get()) }
    viewModel { RegisterViewModel(registerUserUseCase = get()) }
    viewModel { AdminViewModel(adminUseCase = get()) }
    viewModel { SplashViewModel(splashUseCase = get()) }
}