package com.sergiom.gnb.data.remote

import javax.inject.Inject

class GNBRemoteDataSource @Inject constructor(
    private val eventService: GNBService
): BaseDataSource() {
    suspend fun getCurrency() = getResult { eventService.getAllCurrencies() }

    suspend fun getTransactions() = getResult { eventService.getAllTransactions() }
}