package com.dicoding.socialsnap.data.retrofit

import com.dicoding.socialsnap.data.response.DetailSocialResponse
import com.dicoding.socialsnap.data.response.ItemsItem
import com.dicoding.socialsnap.data.response.SocialResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    @Headers("Authorization: token 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")
    fun getUsers(): Call<List<ItemsItem>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsers(@Query("q") query: String): Call<SocialResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String): Call<DetailSocialResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}
