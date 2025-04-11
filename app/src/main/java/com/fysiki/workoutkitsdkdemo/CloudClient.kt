package com.fysiki.workoutkitsdkdemo

import android.content.Context
import android.os.Build
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.fysiki.workoutkit.utils.DeviceIdHelper
import okhttp3.OkHttpClient


object CloudClient {
    private lateinit var applicationContext: Context
    private lateinit var serverUrl: String

    // Initialize with context from your Application class
    fun initialize(context: Context, serverUrl: String) {
        applicationContext = context.applicationContext
        this.serverUrl = serverUrl
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

    val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .okHttpClient(okHttpClient)
            .serverUrl(serverUrl)
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