package org.expenny.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import timber.log.Timber
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideECBHttpClient(): HttpClient = HttpClient(OkHttp) {
        val baseUrl = "https://data-api.ecb.europa.eu/service/data/EXR/"

        defaultRequest {
            url.takeFrom(URLBuilder()
                .takeFrom(baseUrl)
                .apply { encodedPath += url.encodedPath }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.i(message)
                }
            }
        }

        engine {
            config {
                connectTimeout(Duration.of(30, ChronoUnit.SECONDS))
                callTimeout(Duration.of(30, ChronoUnit.SECONDS))
            }
        }

        expectSuccess = true
    }
}