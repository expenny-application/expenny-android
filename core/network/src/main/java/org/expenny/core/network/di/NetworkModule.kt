package org.expenny.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.expenny.core.datastore.BuildConfig
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.network.dto.GoCardlessTokenDto
import timber.log.Timber
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("gocardless")
    @Singleton
    fun provideGoCardlessHttpClient(preferences: ExpennyDataStore): HttpClient {
        return HttpClient(OkHttp) {
            val baseUrl = "https://bankaccountdata.gocardless.com/api/v2/"

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = preferences.getGoCardlessAccessToken().first().orEmpty()
                        val refreshToken = preferences.getGoCardlessRefreshToken().first().orEmpty()
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }
                    refreshTokens {
                        val url = (baseUrl + "token/new/").toHttpUrl().toUrl()
                        val tokenData = client.post(url) {
                            contentType(ContentType.Application.Json)
                            setBody(
                                mapOf(
                                    "secret_id" to BuildConfig.GOCARDLESS_SECRET_ID,
                                    "secret_key" to BuildConfig.GOCARDLESS_SECRET_KEY
                                )
                            )
                        }.body<GoCardlessTokenDto>()

                        preferences.setGoCardlessAccessToken(tokenData.access)
                        preferences.setGoCardlessRefreshToken(tokenData.refresh)

                        BearerTokens(
                            accessToken = tokenData.access,
                            refreshToken = tokenData.refresh
                        )
                    }
                }
            }

            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("GoCardless Http Client").i(message)
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpCache)

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
        }
    }

    @Provides
    @Named("ecb")
    @Singleton
    fun provideEcbHttpClient(): HttpClient = HttpClient(OkHttp) {
        val baseUrl = "https://data-api.ecb.europa.eu/service/data/EXR/"

        defaultRequest {
            url.takeFrom(
                URLBuilder()
                    .takeFrom(baseUrl)
                    .apply { encodedPath += url.encodedPath }
            )
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("Ecb Http Client").i(message)
                }
            }
            level = LogLevel.ALL
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