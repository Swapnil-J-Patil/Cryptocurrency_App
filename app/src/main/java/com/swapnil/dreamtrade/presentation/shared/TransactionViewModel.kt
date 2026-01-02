package com.swapnil.dreamtrade.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.dreamtrade.common.Resource
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.swapnil.dreamtrade.domain.model.TransactionData
import com.swapnil.dreamtrade.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.swapnil.dreamtrade.domain.use_case.transactions.DeleteAllTransactionsUseCase
import com.swapnil.dreamtrade.domain.use_case.transactions.DeleteTransactionUseCase
import com.swapnil.dreamtrade.domain.use_case.transactions.GetAllTransactionsUseCase
import com.swapnil.dreamtrade.domain.use_case.transactions.InsertTransactionUseCase
import com.swapnil.dreamtrade.presentation.home_screen.CoinStatsState
import com.swapnil.dreamtrade.presentation.shared.state.TransactionState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
    private val getAllTransactionUseCase: GetAllTransactionsUseCase,
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val deleteAllTransactionUseCase: DeleteAllTransactionsUseCase,
    ) : ViewModel() {


    private val _coinLivePrice = MutableLiveData<Double>(0.0)
    val coinLivePrice: LiveData<Double> = _coinLivePrice

    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

    private val _transactionState = mutableStateOf(TransactionState())
    val transactionState: State<TransactionState> = _transactionState

    private var fetchJob: Job? = null

    fun startFetchingCoinStats(id:Int) {
        fetchJob?.cancel()  // Ensure previous job is canceled before starting a new one
        fetchJob = viewModelScope.launch {
            while (isActive) { // Ensures cancellation if ViewModel is cleared
                getCoinStats(id)
                delay(2000)
            }
        }
    }

    fun stopFetchingCoinStats() {
        fetchJob?.cancel()
    }

    fun getCoinStats(id: Int) {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val allCoins = result.data?.data?.cryptoCurrencyList ?: emptyList()

                    // Get the list of IDs from currencyList

                    // Extract only the prices of the selected coins
                    val filteredPrice = allCoins.find { it.id == id }?.quotes?.firstOrNull()?.price

                    //Log.d("coinIssue", "prices: $filteredPrice ")

                    // Update state with ordered filtered prices
                    _coinLivePrice.value = filteredPrice?:0.0
                }

                is Resource.Error -> {
                    _statsState.value = CoinStatsState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _statsState.value = CoinStatsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAllTransactions(index: Int) {
        viewModelScope.launch {
            getAllTransactionUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _transactionState.value = TransactionState(isLoading = true)
                        Log.d("transactionViewModel", "Loading data...")
                    }

                    is Resource.Success -> {
                        _transactionState.value = TransactionState(isLoading = true)
                        val sortedTransactions=when (index) {
                            1 -> result.data?.sortedByDescending { it.quantity }
                            2 -> result.data?.sortedByDescending { it.usd }
                            else -> result.data?.sortedByDescending { it.id }
                        }
                       // val sortedTransactions = result.data?.sortedByDescending { it.id }
                        _transactionState.value = TransactionState(isLoading = false)
                        _transactionState.value = TransactionState(transaction = sortedTransactions)
                        Log.d("transactionViewModel", "Successfully loaded: $sortedTransactions")
                    }

                    is Resource.Error -> {
                        _transactionState.value = TransactionState(error = result.message ?: "Unknown error")
                        Log.e("transactionViewModel", "Error loading data: ${result.message}")
                    }
                }
            }
        }
    }

    fun addTransaction(transaction: TransactionData) {
        viewModelScope.launch {
            insertTransactionUseCase(transaction).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("transactionViewModel", "Inserting transaction...")
                    is Resource.Success -> Log.d("transactionViewModel", "Successfully inserted: ${transaction.coinName}")
                    is Resource.Error -> Log.e("transactionViewModel", "Error inserting transaction: ${result.message}")
                }
            }
        }
    }

    suspend fun deleteAllTransactions() {
        viewModelScope.launch {
            deleteAllTransactionUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("transactionViewModel", "Inserting crypto...")
                    is Resource.Success -> Log.d("transactionViewModel", "Successfully Deleted All transactions:")
                    is Resource.Error -> Log.e("transactionViewModel", "Error inserting transaction: ${result.message}")
                }
            }
        }
    }

    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            deleteTransactionUseCase(id).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("transactionViewModel", "Deleting crypto...")
                    is Resource.Success -> Log.d("transactionViewModel", "Successfully deleted: ${id}")
                    is Resource.Error -> Log.e("transactionViewModel", "Error deleting transaction: ${result.message}")
                }
            }
        }
    }
}