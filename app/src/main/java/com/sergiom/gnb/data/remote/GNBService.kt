package com.sergiom.gnb.data.remote

import com.sergiom.gnb.data.entities.GnbCurrency
import com.sergiom.gnb.data.entities.GnbTransaction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface GNBService {
    @Headers( "Accept: application/json",
        "Content-type:application/json")
    @GET("rates")
    suspend fun getAllCurrencies() : Response<List<GnbCurrency>>

    @Headers( "Accept: application/json",
        "Content-type:application/json")
    @GET("transactions")
    suspend fun getAllTransactions() : Response<List<GnbTransaction>>
}