package com.example.madcamp_2nd.fb_app.tab2_fb

import com.example.madcamp_2nd.fb_app.pojo.*
import com.facebook.AccessToken
import com.google.gson.JsonArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

val accessToken = AccessToken.getCurrentAccessToken()
var FACE_BOOK_ID = accessToken.userId

internal interface APIInterface {
    @POST("gallery/FACE_BOOK_ID")
    fun createPhoto(@Body photo: Photo?): Call<Photo?>?

    @GET("gallery/FACE_BOOK_ID")
    fun getAll(@Query("gallery") gallery: ArrayList<JSONObject>?): Call<JSONObject>?


    @GET("gallery/1561181117364134")
    fun getAll(): Call<ArrayList<String>>?

    @POST("gallery/images")
    fun createUser(@Body user: User?): Call<User?>?

    @GET("gallery/")
//    fun getallphoto(@Query("gallery") gallery: List?): Call<MultipleResource?>?

    @FormUrlEncoded
    @POST("contacts/items")
    fun doCreateUserWithField(@Field("name") name: String?, @Field("number"
        ) number: String?
    ): Call<UserList?>? /*
    @GET(“api주소”)
    Call<ResponseBody>함수이름(@Query(“변수이름”), 안드로이드에서 보낼 변수)
    */
}