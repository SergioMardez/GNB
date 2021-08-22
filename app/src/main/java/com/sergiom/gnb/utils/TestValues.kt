package com.sergiom.gnb.utils

import com.sergiom.gnb.data.entities.GnbCurrency
import com.sergiom.gnb.data.entities.GnbTransaction

class TestValues {

    fun getCurrencyTestValues(): List<GnbCurrency> {
        val currencyTestList = mutableListOf<GnbCurrency>()
        var currency = GnbCurrency("EUR", "USD", 1.359)
        currencyTestList.add(currency)
        currency = GnbCurrency("CAD", "EUR", 0.732)
        currencyTestList.add(currency)
        currency = GnbCurrency("USD", "EUR", 0.736)
        currencyTestList.add(currency)
        currency = GnbCurrency("EUR", "CAD", 1.366)
        currencyTestList.add(currency)
        return currencyTestList
    }

    fun getTransactionTestValues(): List<GnbTransaction> {
        val transactionTestList = mutableListOf<GnbTransaction>()
        var transaction = GnbTransaction("T2006", 10.00, "USD")
        transactionTestList.add(transaction)
        transaction = GnbTransaction("M2007", 34.57, "CAD")
        transactionTestList.add(transaction)
        transaction = GnbTransaction("R2008", 17.95, "USD")
        transactionTestList.add(transaction)
        transaction = GnbTransaction("T2006", 7.63, "EUR")
        transactionTestList.add(transaction)
        transaction = GnbTransaction("B2009", 21.23, "USD")
        transactionTestList.add(transaction)
        return transactionTestList
    }
}