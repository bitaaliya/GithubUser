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
