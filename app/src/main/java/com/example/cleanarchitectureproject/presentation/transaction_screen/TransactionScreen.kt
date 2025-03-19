package com.example.cleanarchitectureproject.presentation.transaction_screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cleanarchitectureproject.data.local.shared_prefs.PrefsManager
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.model.SweetToastProperty
import com.example.cleanarchitectureproject.presentation.auth_screen.Error
import com.example.cleanarchitectureproject.presentation.common_components.CustomSweetToast
import com.example.cleanarchitectureproject.presentation.shared.PortfolioViewModel
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.ConfirmationPopup
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.CurrencyCard
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.DraggableCards
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.example.cleanarchitectureproject.presentation.ui.theme.red
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TransactionScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    transaction: String,
    coin: CryptoCoin,
    context: Context,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTab = configuration.screenWidthDp.dp > 600.dp
    val adaptiveHeight = if (screenWidth > 600.dp) 100.dp else 75.dp
    val prefsManager = remember { PrefsManager(context) }
    val dollars = prefsManager.getDollarAmount()
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("Success!") }
    val coroutineScope = rememberCoroutineScope()
    var toastType by remember { mutableStateOf<SweetToastProperty>(Error()) }
    var visibility by remember {
        mutableStateOf(false)
    }
    var isTransaction by remember {
        mutableStateOf(false)
    }
    var isBuyClicked by remember {
        mutableStateOf(false)
    }
    val cryptoQuantity = remember {
        mutableStateOf("")
    }
    val amountOfDollars = remember {
        mutableStateOf("")
    }
    var savedQuantity by remember { mutableStateOf<Double?>(0.0) }

    LaunchedEffect(Unit) {
        visibility = true
    }
    LaunchedEffect(savedQuantity) {
        viewModel.getSavedCoinQuantity(coin.id.toString()) { quantity ->
            Log.d("savedQuantity", "TransactionScreen: $quantity")
            savedQuantity = quantity  // Update state to trigger recomposition
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 45.dp),
        ) {
            item {
                CurrencyCard(
                    currency = coin.name,
                    image = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adaptiveHeight)
                        .padding(horizontal = 15.dp),
                    animatedVisibilityScope = animatedVisibilityScope,
                    currencyId = coin.id.toString(),
                    listType = transaction,
                    symbol = coin.symbol.toString(),
                    graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png"

                )
            }
            item {
                AnimatedVisibility(
                    visible = visibility,
                    enter = slideInVertically(
                        initialOffsetY = { it }, // Starts from full height (bottom)
                        animationSpec = tween(
                            durationMillis = 800, // Slightly increased duration for a smoother feel
                            easing = LinearOutSlowInEasing // Smoother easing for entering animation
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }, // Moves to full height (bottom)
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = FastOutSlowInEasing // Keeps a natural exit motion
                        )
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    dollars?.toDouble()?.let {
                        DraggableCards(
                            coin = coin,
                            imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                            context = context,
                            transaction = transaction,
                            isBuyClicked = { flag, quantity, usd ->
                                isBuyClicked = flag
                                isTransaction = true
                                cryptoQuantity.value = quantity
                                amountOfDollars.value = usd
                            },
                            savedQuantity=savedQuantity,
                            dollars = it
                        )
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = isTransaction,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            exit = scaleOut()
        ) {
            ConfirmationPopup(
                onCancel = { isTransaction = false },
                onConfirm = {
                    val cleanQuantity = cryptoQuantity.value.replace(",", "").toDouble()
                    val cleanDollars = amountOfDollars.value.replace(",", "").toDouble()

                    val availableDollars = dollars?.replace(",", "")?.toDouble()?: 0.0
                    val availableQuantity =savedQuantity?: 0.0

                    if (isBuyClicked) {
                        val remainingUsd = (availableDollars - cleanDollars)
                        val sum = if (savedQuantity == null) cleanQuantity else savedQuantity?.plus(cleanQuantity)

                        if(remainingUsd < 0)
                        {
                            showToast = false // Dismiss current toast
                            coroutineScope.launch { // Ensure state updates properly
                                delay(100) // Small delay to allow recomposition
                                toastMessage = "Insufficient balance! Add more dollars to proceed."
                                toastType = Error()
                                showToast = true
                            }
                        }
                        else
                        {
                            val coinData = PortfolioCoin(
                                id = coin.id,
                                name = coin.name,
                                symbol = coin.symbol,
                                slug = coin.slug,
                                tags = coin.tags,
                                cmcRank = coin.cmcRank,
                                marketPairCount = coin.marketPairCount,
                                circulatingSupply = coin.circulatingSupply,
                                selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply,
                                totalSupply = coin.totalSupply,
                                maxSupply = coin.maxSupply,
                                isActive = coin.isActive,
                                lastUpdated = coin.lastUpdated,
                                dateAdded = coin.dateAdded,
                                quotes = coin.quotes,
                                isAudited = coin.isAudited,
                                badges = coin.badges ?: emptyList(),
                                quantity = sum
                            )

                            viewModel.addCrypto(coinData)
                            prefsManager.setDollarAmount(remainingUsd.toString())

                            viewModel.getSavedCoinQuantity(coin.id.toString()) { quantity ->
                                Log.d("savedQuantity", "TransactionScreen: $quantity")
                                savedQuantity = quantity  // Update state to trigger recomposition
                            }
                        }

                    }
                    else{
                        val remainingQuantity=(availableQuantity - cleanQuantity)
                        val remainingUsd = (availableDollars + cleanDollars)

                        val diff= if(savedQuantity == null) 0.0 else savedQuantity?.minus(cleanQuantity)
                        if(cleanQuantity==0.0)
                        {
                            showToast = false // Dismiss current toast
                            coroutineScope.launch { // Ensure state updates properly
                                delay(100) // Small delay to allow recomposition
                                toastMessage = "You have no coins to sell!"
                                toastType = Error()
                                showToast = true
                            }
                        }
                        else if(remainingQuantity < 0)
                        {
                            showToast = false // Dismiss current toast
                            coroutineScope.launch { // Ensure state updates properly
                                delay(100) // Small delay to allow recomposition
                                toastMessage = "Insufficient balance! Add more coins to proceed."
                                toastType = Error()
                                showToast = true
                            }
                        }
                        else
                        {
                            val coinData = PortfolioCoin(
                                id = coin.id,
                                name = coin.name,
                                symbol = coin.symbol,
                                slug = coin.slug,
                                tags = coin.tags,
                                cmcRank = coin.cmcRank,
                                marketPairCount = coin.marketPairCount,
                                circulatingSupply = coin.circulatingSupply,
                                selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply,
                                totalSupply = coin.totalSupply,
                                maxSupply = coin.maxSupply,
                                isActive = coin.isActive,
                                lastUpdated = coin.lastUpdated,
                                dateAdded = coin.dateAdded,
                                quotes = coin.quotes,
                                isAudited = coin.isAudited,
                                badges = coin.badges ?: emptyList(),
                                quantity = diff
                            )

                            viewModel.addCrypto(coinData)
                            prefsManager.setDollarAmount(remainingUsd.toString())

                            viewModel.getSavedCoinQuantity(coin.id.toString()) { quantity ->
                                Log.d("savedQuantity", "TransactionScreen: $quantity")
                                savedQuantity = quantity  // Update state to trigger recomposition
                            }
                        }
                    }

                },
                color = if (isBuyClicked) green else red,
                usd = amountOfDollars.value,
                quantity = cryptoQuantity.value,
                imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                isBuy = isBuyClicked,
                symbol = coin.symbol!!,
                isTab = isTab
            )
        }
        CustomSweetToast(
            message = toastMessage,
            type = toastType,
            onDismiss = { showToast = !showToast },
            visibility = showToast
        )
    }
}

