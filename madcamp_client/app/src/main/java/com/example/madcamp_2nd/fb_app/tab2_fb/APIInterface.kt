package com.example.madcamp_2nd.fb_app.tab2_fb

import com.example.madcamp_2nd.fb_app.pojo.MultipleResource
import com.example.madcamp_2nd.fb_app.pojo.Photo
import com.example.madcamp_2nd.fb_app.pojo.User
import com.example.madcamp_2nd.fb_app.pojo.UserList
import retrofit2.Call
import retrofit2.http.*

internal interface APIInterface {
    @GET("gallery/images")
    fun getimages(): Call<MultipleResource?>?

    @POST("gallery/images")
    fun createUser(@Body user: User?): Call<User?>?

    @POST("gallery/images")
    fun createPhoto(@Body photo: Photo?): Call<Photo?>?

    @GET("gallery/images")
    fun doGetUserList(@Query("page") page: String?): Call<UserList?>?

    @GET("gallery/images")
    fun getallphoto(@Query("photo") photo: String?): Call<MultipleResource?>?

    @FormUrlEncoded
    @POST("contacts/items")
    fun doCreateUserWithField(
        @Field("name") name: String?, @Field(
            "number"
        ) number: String?
    ): Call<UserList?>? /*
    @GET(“api주소”)
    Call<ResponseBody>함수이름(@Query(“변수이름”), 안드로이드에서 보낼 변수)
    */
}