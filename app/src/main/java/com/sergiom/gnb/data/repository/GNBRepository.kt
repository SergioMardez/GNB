package com.sergiom.gnb.data.repository

import com.sergiom.gnb.data.remote.GNBRemoteDataSource
import javax.inject.Inject

class GNBRepository @Inject constructor(
    private val remoteDataSource: GNBRemoteDataSource
) {
    suspend fun getCurrency() = remoteDataSource.getCurrency()

    suspend fun getTransactions() = remoteDataSource.getTransactions()
}