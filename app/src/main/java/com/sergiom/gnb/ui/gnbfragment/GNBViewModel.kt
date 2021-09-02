package com.sergiom.gnb.ui.gnbfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiom.gnb.data.entities.GnbCurrency
import com.sergiom.gnb.data.entities.GnbTransaction
import com.sergiom.gnb.data.repository.GNBRepository
import com.sergiom.gnb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import com.sergiom.gnb.MainApplication
import com.sergiom.gnb.R
import com.sergiom.gnb.utils.TestValues


@HiltViewModel
class GNBViewModel@Inject constructor(private val repository: GNBRepository) : ViewModel()  {

    private lateinit var currencyList: List<GnbCurrency>
    private lateinit var transactionList: List<GnbTransaction>
    val mTransactionList: MutableLiveData<List<GnbTransaction>> = MutableLiveData()
    val mError: MutableLiveData<String> = MutableLiveData()
    private val toEuroList = mutableListOf<GnbCurrency>()
    private val toCurrencyToEuro = mutableListOf<GnbCurrency>()

    init {
        getCurrency()
        getTransactions()
    }

    private fun getCurrency() {
        viewModelScope.launch {
            val responseStatus = repository.getCurrency()

            if (responseStatus.status == Resource.Status.SUCCESS) {
                //currencyList = TestValues().getCurrencyTestValues() //Example data
                currencyList = responseStatus.data?: listOf()
                setCurrency()
            } else if (responseStatus.status == Resource.Status.ERROR) {
                mError.value = responseStatus.message?: "Network Error"
            }
        }
    }

    private fun setCurrency() {
        for (currency in currencyList) {
            if (currency.to == "EUR") {
                toEuroList.add(currency)
            }
        }

        for (currency in currencyList) {
            for (toEuro in toEuroList) {
                if (currency.to == toEuro.from && currency.from != "EUR") {
                    toCurrencyToEuro.add(currency)
                }
            }
        }
    }

    fun getCurrencyForMenu(): List<String> {
        val currencyStrings = mutableListOf<String>()

        try {
            for (currency in currencyList) {
                val string = MainApplication.instance.applicationContext?.getString(
                    R.string.currency_string,
                    currency.from, currency.to, currency.rate.toString()
                )
                string?.let { currencyStrings.add(it) }
            }
        } catch (ignored: UninitializedPropertyAccessException) {}

        return currencyStrings
    }

    private fun getTransactions() {
        viewModelScope.launch {
            val responseStatus = repository.getTransactions()

            if (responseStatus.status == Resource.Status.SUCCESS) {
                //transactionList = TestValues().getTransactionTestValues() //Example data
                transactionList = responseStatus.data?: listOf()
                mTransactionList.value = transactionList
            } else if (responseStatus.status == Resource.Status.ERROR) {
                mError.value = responseStatus.message?: "Network Error"
            }
        }
    }

    fun getAllTransactions(transaction: GnbTransaction): List<GnbTransaction> {
        val sameTransactions = mutableListOf<GnbTransaction>()

        for (transactionInList in transactionList) {
            if (transaction.sku == transactionInList.sku) {
                sameTransactions.add(transactionInList)
            }
        }

        return sameTransactions
    }

    fun getAllEuros(transactions: List<GnbTransaction>): Double {
        var amount = 0.0

        for (trans in transactions) {
            amount += getEuroValue(trans)
        }

        return formatAmount(amount)
    }

    private fun getEuroValue(transaction: GnbTransaction): Double {
        var amount = 0.0

        if (transaction.currency == "EUR") {
            amount = transaction.amount
        }

        //Direct conversion to euro
        if (amount == 0.0) {
            for (euro in toEuroList) {
                if (transaction.currency == euro.from) {
                    amount = formatAmount(transaction.amount * euro.rate)
                }
            }
        }

        //Convert to currency with direct conversion
        if (amount == 0.0) {
            for (currencyToEuro in toCurrencyToEuro) {
                if (transaction.currency == currencyToEuro.from) {
                    amount = formatAmount(transaction.amount * currencyToEuro.rate)

                    for (euro in toEuroList) {
                        if (currencyToEuro.to == euro.from) {
                            amount = formatAmount(amount * euro.rate)
                        }
                    }
                }
            }
        }

        return amount
    }

    private fun formatAmount(amount: Double): Double {
        val formatter: NumberFormat = NumberFormat.getNumberInstance()
        formatter.maximumFractionDigits = 2
        val amount = formatter.format(amount).replace(",", "")
        return amount.toDouble()
    }
}