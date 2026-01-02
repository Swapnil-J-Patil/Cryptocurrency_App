package com.swapnil.dreamtrade.presentation.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.swapnil.dreamtrade.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
) : ViewModel() {

    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

    private val _topGainers = MutableStateFlow<List<CryptoCurrencyCM>>(emptyList())
    val topGainers: StateFlow<List<CryptoCurrencyCM>> = _topGainers

    private val _topLosers = MutableStateFlow<List<CryptoCurrencyCM>>(emptyList())
    val topLosers: StateFlow<List<CryptoCurrencyCM>> = _topLosers

    private val _gainerPercentageList = MutableStateFlow<List<String>>(emptyList())
    val gainerPercentageList: StateFlow<List<String>> = _gainerPercentageList

    private val _gainerPriceList = MutableStateFlow<List<String>>(emptyList())
    val gainerPriceList: StateFlow<List<String>> = _gainerPriceList

    private val _gainerLogoList = MutableStateFlow<List<String>>(emptyList())
    val gainerLogoList: StateFlow<List<String>> = _gainerLogoList

    private val _gainerGraphList = MutableStateFlow<List<String>>(emptyList())
    val gainerGraphList: StateFlow<List<String>> = _gainerGraphList

    private val _loserPercentageList = MutableStateFlow<List<String>>(emptyList())
    val loserPercentageList: StateFlow<List<String>> = _loserPercentageList

    private val _loserPriceList = MutableStateFlow<List<String>>(emptyList())
    val loserPriceList: StateFlow<List<String>> = _loserPriceList

    private val _loserLogoList = MutableStateFlow<List<String>>(emptyList())
    val loserLogoList: StateFlow<List<String>> = _loserLogoList

    private val _loserGraphList = MutableStateFlow<List<String>>(emptyList())
    val loserGraphList: StateFlow<List<String>> = _loserGraphList

    private var fetchJob: Job? = null

    init {
        getCoinStats()
    }

    fun startFetchingCoinStats() {
        fetchJob?.cancel()  // Ensure previous job is canceled before starting a new one
        fetchJob = viewModelScope.launch {
            while (isActive) { // Ensures cancellation if ViewModel is cleared
                getLivePrices()
                delay(10000)
            }
        }
    }

    fun stopFetchingCoinStats() {
        fetchJob?.cancel()
    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _statsState.value = CoinStatsState(cryptocurrency = result.data)
                    //Log.d("cryptoCurrency", "getCoin: ${result.data}")
                    getGainers(_statsState.value.cryptocurrency!!.data.cryptoCurrencyList)
                    getLosers(_statsState.value.cryptocurrency!!.data.cryptoCurrencyList)
                    processGainersAndLosers(_topGainers.value, _topLosers.value)
                }

                is Resource.Error -> {
                    _statsState.value = CoinStatsState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _statsState.value = CoinStatsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getLivePrices() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val allCoins = result.data?.data?.cryptoCurrencyList ?: emptyList()
                    val dfSmall = DecimalFormat("0.####") // Up to 6 decimal places

                    // Get the list of IDs from currencyList
                    val gainerCoinIds = _topGainers.value?.map { it.id } ?: emptyList()
                    // Extract only the prices of the selected coins
                    val filteredPricesGainers = gainerCoinIds.mapNotNull { id ->
                        allCoins.find { it.id == id }?.quotes?.firstOrNull()?.price
                    }
                    //Log.d("coinIssue", "prices: $filteredPricesGainers ")
                    // Update state with ordered filtered prices
                    _gainerPriceList.value = filteredPricesGainers.map { price ->
                        val formattedPrice = when {
                            price < 0.0001 -> "0.00.."  // Extremely small values
                            price < 100 -> dfSmall.format(price) // Up to 6 decimal places
                            else -> price.toInt().toString().take(3) + ".." // Large numbers (first 3 digits + "..")
                        }
                        "$ $formattedPrice"
                    }


                    val losersCoinIds = _topLosers.value?.map { it.id } ?: emptyList()
                    // Extract only the prices of the selected coins
                    val filteredPricesLosers = losersCoinIds.mapNotNull { id ->
                        allCoins.find { it.id == id }?.quotes?.firstOrNull()?.price
                    }
                    //Log.d("coinIssue", "prices: $filteredPricesLosers ")
                    // Update state with ordered filtered prices
                    _loserPriceList.value = filteredPricesLosers.map { price ->
                        val formattedPrice = when {
                            price < 0.0001 -> "0.00.."  // Extremely small values
                            price < 100 -> dfSmall.format(price) // Up to 6 decimal places
                            else -> price.toInt().toString().take(3) + ".." // Large numbers (first 3 digits + "..")
                        }
                        "$ $formattedPrice"
                    }
                }

                is Resource.Error -> {
                   /* _statsState.value = CoinStatsState(
                        error = result.message ?: "An unexpected error occurred"
                    )*/
                }

                is Resource.Loading -> {
                    //_statsState.value = CoinStatsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun processGainersAndLosers(topGainers: List<CryptoCurrencyCM>, topLosers: List<CryptoCurrencyCM>) {
        val dfSmall = DecimalFormat("0.####") // Up to 6 decimal places
        val dfPercentage = DecimalFormat("#,##0.00") // Ensures percentages are formatted properly

        viewModelScope.launch {
            _gainerPercentageList.value = topGainers.map { gainer ->
                val percentage = gainer.quotes[0].percentChange1h
                val formattedPercentage = dfPercentage.format(percentage) + " %"
                if (percentage >= 0) "+$formattedPercentage" else formattedPercentage
            }

            _gainerPriceList.value = topGainers.map { gainer ->
                val price = gainer.quotes[0].price
                val formattedPrice = when {
                    price < 0.0001 -> "0.00.."  // Extremely small values
                    price < 100 -> dfSmall.format(price) // Up to 6 decimal places
                    else -> price.toInt().toString().take(3) + ".." // Large numbers (first 3 digits + "..")
                }
                "$ $formattedPrice"
            }

            _gainerLogoList.value = topGainers.map { gainer ->
                "https://s2.coinmarketcap.com/static/img/coins/64x64/${gainer.id}.png"
            }

            _gainerGraphList.value = topGainers.map { gainer ->
                "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${gainer.id}.png"
            }

            _loserPercentageList.value = topLosers.map { loser ->
                val percentage = loser.quotes[0].percentChange1h
                dfPercentage.format(percentage) + " %"
            }

            _loserPriceList.value = topLosers.map { loser ->
                val price = loser.quotes[0].price
                val formattedPrice = when {
                    price < 0.0001 -> "0.00.."  // Extremely small values
                    price < 1000 -> dfSmall.format(price) // Up to 6 decimal places
                    else -> price.toInt().toString().take(3) + ".." // Large numbers (first 3 digits + "..")
                }
                "$ $formattedPrice"
            }

            _loserLogoList.value = topLosers.map { loser ->
                "https://s2.coinmarketcap.com/static/img/coins/64x64/${loser.id}.png"
            }

            _loserGraphList.value = topLosers.map { loser ->
                "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${loser.id}.png"
            }
        }
    }

    fun getGainers(cryptoCurrencyList: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            val sortedCryptocurrencies = cryptoCurrencyList?.sortedWith { o1, o2 ->
                // Sort by `percentChange24h` in descending order
                o2.quotes[0].percentChange1h.compareTo(o1.quotes[0].percentChange1h)
            } ?: emptyList()

            // Update state with the sorted list
            _topGainers.value = sortedCryptocurrencies
           // Log.d("cryptoCurrency", "Sorted Gainers: $sortedCryptocurrencies")
        }
    }

    fun getLosers(cryptoCurrencyList: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            val sortedCryptocurrencies = cryptoCurrencyList.sortedWith { o1, o2 ->
                // Sort by `percentChange24h` in ascending order
                o1.quotes[0].percentChange1h.compareTo(o2.quotes[0].percentChange1h)
            }

            // Update state with the sorted list
            _topLosers.value = sortedCryptocurrencies
           // Log.d("cryptoCurrency", "Sorted Losers: $sortedCryptocurrencies")
        }
    }
    fun formatPrice(value: Double): String {
        val priceStr = value.toBigDecimal().toPlainString() // Avoid scientific notation

        return when {
            priceStr.length >= 10 -> priceStr.substring(0, 10)  // Truncate if too long
            else -> priceStr.padEnd(10, '0')  // Pad with zeros if too short
        }
    }

}
