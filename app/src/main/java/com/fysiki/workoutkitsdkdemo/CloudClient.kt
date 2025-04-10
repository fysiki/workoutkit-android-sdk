package com.fysiki.workoutkitsdkdemo

import android.content.Context
import android.os.Build
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.fysiki.workoutkit.utils.DeviceIdHelper
import okhttp3.OkHttpClient


object CloudClient {
    private const val CLOUD_URL = "https://cloud.dev.martha.aws.fizzup.com/graphql"
    private lateinit var applicationContext: Context

    // Initialize with context from your Application class
    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

    val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .okHttpClient(okHttpClient)
            .serverUrl(CLOUD_URL)
            .addHttpHeader(
                "X-WorkoutKit-Device",
                DeviceIdHelper.getDeviceId(applicationContext)
            )
            .addHttpHeader(
                "Accept-Language",
                getLanguage()
            )
            .build()
    }

    private fun getLanguage(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !applicationContext.resources.configuration.locales.isEmpty) {
            applicationContext.resources.configuration.locales.get(0)
        } else {
            applicationContext.resources.configuration.locale
        }
        return locale.toLanguageTag()
    }
}