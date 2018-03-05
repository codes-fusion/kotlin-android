package com.example.rssgrabber.retrofit.services

import com.example.rssgrabber.retrofit.FeedData
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Url

interface FeedService {
    @GET fun get(@Url url: String): Flowable<FeedData>
}