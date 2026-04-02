package com.swapnil.dreamtrade.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.dreamtrade.common.Resource
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.swapnil.dreamtrade.domain.model.PortfolioCoin
import com.swapnil.dreamtrade.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.swapnil.dreamtrade.domain.use_case.portfolio_coins.DeleteCryptoPortfolioUseCase
import com.swapnil.dreamtrade.domain.use_case.portfolio_coins.GetAllCryptoPortfolioUseCase
import com.swapnil.dreamtrade.domain.use_case.portfolio_coins.GetCoinPortfolioUseCase
import com.swapnil.dreamtrade.domain.use_case.portfolio_coins.InsertCryptoPortfolioUseCase
import com.swapnil.dreamtrade.domain.use_case.portfolio_coins.IsCoinSavedPortfolioUseCase
import com.swapnil.dreamtrade.presentation.home_screen.CoinStatsState
import com.swapnil.dreamtrade.presentation.shared.state.PortfolioCoinState
import com.swapnil.dreamtrade.presentation.ui.theme.green
import com.swapnil.dreamtrade.presentation.ui.theme.lightRed

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getAllCryptoUseCase: GetAllCryptoPortfolioUseCase,
    private val getCoinUseCase: GetCoinPortfolioUseCase,
    private val insertCryptoUseCase: InsertCryptoPortfolioUseCase,
    private val deleteCryptoUseCase: DeleteCryptoPortfolioUseCase,
    private val isCoinSavedUseCase: IsCoinSavedPortfolioUseCase,
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,

    ) : ViewModel() {

    private val _coinListState = MutableLiveData(PortfolioCoinState())
    val coinListState: LiveData<PortfolioCoinState> = _coinListState

    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

    private val _currencyList = MutableLiveData<List<PortfolioCoin>>(emptyList())
    val currencyList: LiveData<List<PortfolioCoin>> = _currencyList

    private val _filteredCurrencyList = MutableLiveData<List<Double>>(emptyList())
    val filteredCurrencyList: LiveData<List<Double>> = _filteredCurrencyList

    val portfolioValue = MutableLiveData(0.0)
    val totalInvestment = MutableLiveData(0.0)

    private var fetchJob: Job? = null

    init {
        loadCrypto()

        // Observe filteredCurrencyList and call processCoins only when data is available
        _filteredCurrencyList.observeForever { filteredPrices ->
            val coins = _coinListState.value?.cryptocurrency
            if (!coins.isNullOrEmpty() && filteredPrices.isNotEmpty()) {
                processCoins(coins, filteredPrices)
            }
        }
    }

    fun startFetchingCoinStats() {
        fetchJob?.cancel()  // Ensure previous job is canceled before starting a new one
        fetchJob = viewModelScope.launch {
            while (isActive) { // Ensures cancellation if ViewModel is cleared

                getCoinStats()
                delay(2000)
            }
        }
    }

    fun stopFetchingCoinStats() {
        fetchJob?.cancel()
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            getAllCryptoUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _coinListState.value = PortfolioCoinState(isLoading = true)
                        Log.d("portfolioViewmodelPVM", "Loading data...")
                    }

                    is Resource.Success -> {
                        portfolioValue.value = 0.0
                        totalInvestment.value = 0.0
                        _coinListState.value = PortfolioCoinState(cryptocurrency = result.data)
                        getCoinStats()
                       /* if (!result.data.isNullOrEmpty()) {
                            processCoins(result.data)
                            Log.d("portfolioViewmodelPVM", "Successfully loaded: ${result.data.size}")
                        }*/

                    }

                    is Resource.Error -> {
                        _coinListState.value =
                            PortfolioCoinState(error = result.message ?: "Unknown error")
                        Log.e("portfolioViewmodelPVM", "Error loading data: ${result.message}")
                    }
                }
            }
        }
    }

    fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val allCoins = result.data?.data?.cryptoCurrencyList ?: emptyList()

                    // Get the list of IDs from currencyList
                    val selectedCoinIds =
                        _coinListState.value?.cryptocurrency?.map { it.id } ?: emptyList()

                    // Extract only the prices of the selected coins
                    val filteredPrices = selectedCoinIds.mapNotNull { id ->
                        allCoins.find { it.id == id }?.quotes?.firstOrNull()?.price
                    }
                    Log.d("coinIssue", "prices: $filteredPrices ")

                    // Update state with ordered filtered prices
                    _filteredCurrencyList.value = filteredPrices
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

    fun getSavedCoinQuantity(coinId: String, onResult: (Double?) -> Unit) {
        viewModelScope.launch {
            val isSaved = isCoinSaved(coinId)
            if (isSaved) {
                getCoinUseCase(coinId).collect { result ->
                    when (result) {
                        is Resource.Success -> onResult(result.data?.quantity)
                        else -> onResult(null)
                    }
                }
            } else {
                onResult(null)
            }
        }
    }


    fun addCrypto(crypto: PortfolioCoin) {
        viewModelScope.launch {
            insertCryptoUseCase(crypto).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("SavedCoinViewModel", "Inserting crypto...")
                    is Resource.Success -> Log.d(
                        "SavedCoinViewModel",
                        "Successfully inserted: ${crypto.name}"
                    )

                    is Resource.Error -> Log.e(
                        "SavedCoinViewModel",
                        "Error inserting crypto: ${result.message}"
                    )
                }
            }
        }
    }

    suspend fun isCoinSaved(coinId: String): Boolean {
        return isCoinSavedUseCase(coinId)
    }

    fun removeCrypto(coin: PortfolioCoin) {
        viewModelScope.launch {
            deleteCryptoUseCase(coin).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("SavedCoinViewModel", "Deleting crypto...")
                    is Resource.Success -> Log.d(
                        "SavedCoinViewModel",
                        "Successfully deleted: ${coin.name}"
                    )

                    is Resource.Error -> Log.e(
                        "SavedCoinViewModel",
                        "Error deleting crypto: ${result.message}"
                    )
                }
            }
        }
    }

    private fun processCoins(coins: List<PortfolioCoin>, filteredPrices: List<Double>) {
        Log.d("portfolioViewmodelPVM", "Processing coins...")
        var runningPortfolioValue = 0.0
        var runningTotalInvestment = 0.0

        val cryptocurrencyCoins = coins.mapIndexed { index, coin ->
            val firstQuote = coin.quotes?.firstOrNull()
            val percentage = firstQuote?.percentChange1h?.toString() ?: "0.0"

            // ✅ Use purchasedAt as unit price (per coin price at time of purchase)
            val purchasedUnitPrice = coin.purchasedAt?.toDouble() ?: 0.0
            val livePrice = filteredPrices.getOrNull(index) ?: firstQuote?.price

            // ✅ Multiply by quantity here only — never store the multiplied result back
            val currentPrice = (livePrice ?: 0.0) * (coin.quantity ?: 0.0)
            val purchasedPrice = purchasedUnitPrice * (coin.quantity ?: 0.0)

            val returns = currentPrice - purchasedPrice
            val color = if (returns >= 0) green else lightRed

            val percentageChange = if (purchasedPrice > 0) {
                (returns / purchasedPrice) * 100
            } else {
                0.0
            }

            // ✅ Accumulate into local vars, not LiveData directly
            runningPortfolioValue += currentPrice
            runningTotalInvestment += purchasedPrice

            Log.d("percentageChange", "${coin.name}: currentPrice=$currentPrice purchasedPrice=$purchasedPrice change=$percentageChange%")
            Log.d("percentageChange", "${coin.name}: livePrice=$livePrice purchasedUnitPrice=$purchasedUnitPrice quantity=${coin.quantity}")

            PortfolioCoin(
                id = coin.id,
                name = coin.name,
                symbol = coin.symbol.toString(),
                slug = coin.slug.toString(),
                tags = coin.tags ?: emptyList(),
                cmcRank = coin.cmcRank ?: 0,
                marketPairCount = coin.marketPairCount ?: 0,
                circulatingSupply = coin.circulatingSupply ?: 0.0,
                selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply ?: 0.0,
                totalSupply = coin.totalSupply ?: 0.0,
                maxSupply = coin.maxSupply,
                isActive = coin.isActive ?: 0,
                lastUpdated = coin.lastUpdated.toString(),
                dateAdded = coin.dateAdded.toString(),
                quotes = coin.quotes,
                isAudited = coin.isAudited ?: false,
                badges = coin.badges ?: emptyList(),
                purchasedAt = purchasedUnitPrice, // ✅ Always store unit price, NOT total
                currentPrice = currentPrice,
                logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png",
                color = color,
                isGainer = returns >= 0,
                quantity = coin.quantity,
                returnPercentage = percentageChange,
                returns = returns
            )
        }

        // ✅ Set LiveData once after the loop, not inside it
        portfolioValue.value = runningPortfolioValue
        totalInvestment.value = runningTotalInvestment
        _currencyList.value = cryptocurrencyCoins
    }
}