package uz.gka.rsprm.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gka.rsprm.BuildConfig

object ApiFactory {
    private var sClient: OkHttpClient? = null

    @Volatile
    private var sService: RsprmService? = null

    fun getRsprmService(): RsprmService {
        var service = sService
        if (service == null) {
            synchronized(ApiFactory::class.java) {
                service = sService
                if (service == null) {
                    sService = buildRetrofit().create(RsprmService::class.java)
                    service = sService
                }
            }
        }
        return service!!
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getClient(): OkHttpClient {
        var client = sClient
        if (client == null) {
            synchronized(ApiFactory::class.java) {
                client = sClient
                if (client == null) {
                    sClient = buildClient()
                    client = sClient
                }
            }
        }
        return client!!
    }

    private fun buildClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }


}
