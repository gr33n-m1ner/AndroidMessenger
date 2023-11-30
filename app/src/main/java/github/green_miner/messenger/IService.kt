package github.green_miner.messenger

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface IService {
    @GET("/channel/{channel}")
    fun getMessages(
        @Path("channel") channel : String,
        @Query("lastKnownId") lastKnownId: Int,
        @Query("limit") limit: Int = 50
    ): Call<List<Message>>

    @GET("/channels")
    fun getChannels() : Call<List<String>>

    @POST("/1ch")
    fun postMessage(@Body msg: Message): Call<ResponseBody>

    @Multipart
    @POST("/1ch")
    fun postImage(
        @Part("msg") msg: RequestBody,
        @Part pic: MultipartBody.Part
    ): Call<ResponseBody>
}