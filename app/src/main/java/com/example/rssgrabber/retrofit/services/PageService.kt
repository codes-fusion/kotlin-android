package com.example.rssgrabber.retrofit.services

import com.example.rssgrabber.retrofit.entities.PageInfo
import com.example.rssgrabber.retrofit.entities.VacancyInfo
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Url

interface PageService {
    @GET fun get(@Url url: String): Flowable<PageInfo>
    @GET fun getVacancy(@Url url: String): Flowable<VacancyInfo>
}