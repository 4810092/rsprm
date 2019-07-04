package uz.gka.rsprm.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface RsprmService {

    @GET("test/trucks")
    fun getTrucks(): Single<Response<ArrayList<TruckModel>>>

    @POST("test/truck/add")
    fun addTruck(@Body truckModel: TruckModel): Single<Response<TruckModel>>

    @PATCH("test/truck/{id}")
    fun updateTruck(@Path("id") id: String, @Body truckModel: TruckModel): Single<Response<TruckModel>>

    @DELETE("test/truck/{id}")
    fun deleteTruck(@Path("id") id: String): Single<Response<Any>>


}