package com.example.cleanarchitectureproject.presentation.profile_screen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.shared.TransactionViewModel
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TransactionsCard(
    modifier: Modifier = Modifier,
    transactionViewModel: TransactionViewModel = hiltViewModel()
    ) {

    val transactionState = transactionViewModel.transactionState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    /*LaunchedEffect(Unit) {
        transactionViewModel.getAllTransactions()
    }*/
    var isPlaying by remember { mutableStateOf(true) } // Control animation state
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying // Infinite repeat mode
    )

   // var isFilterClicked by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        transactionViewModel.getAllTransactions(0)
    }

    val scale by animateFloatAsState(
        targetValue = if (!(transactionState.error.isNotBlank() || transactionState.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    ) {
        val listState = rememberLazyListState()
        AnimatedVisibility(
            visible = !(transactionState.error.isNotBlank() || transactionState.isLoading),
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            ) {
                transactionState.transaction?.let { transactions ->
                    if (transactions.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(y=-16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(green.copy(alpha = 0.2f))
                                    .padding(horizontal = 14.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    Modifier
                                        .weight(0.3f)
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "Transaction",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Row(
                                    Modifier
                                        .weight(0.3f)
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "Date",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Row(
                                    Modifier
                                        .weight(0.2f)
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "QTY",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Row(
                                    Modifier
                                        .weight(0.3f)
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "USD",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = listState,
                            ) {
                                itemsIndexed(
                                    transactionState.transaction,
                                    key = { _, coin -> coin.id }
                                ) { index, transaction ->
                                    val isVisible = remember {
                                        derivedStateOf {
                                            val visibleItems = listState.layoutInfo.visibleItemsInfo
                                            visibleItems.any { it.index == index }
                                        }
                                    }
                                    val scale = remember { Animatable(0f) }

                                    LaunchedEffect(isVisible.value) {
                                        if (isVisible.value) {
                                            scale.animateTo(
                                                targetValue = 1f,
                                                animationSpec = tween(
                                                    durationMillis = 300,
                                                    easing = FastOutSlowInEasing
                                                )
                                            )
                                        } else {
                                            scale.snapTo(0f)
                                        }
                                    }

                                    transaction.usd?.let {
                                        TransactionCardItem(
                                            usd = it,
                                            color = if (transaction.transaction == "Buy") green else lightRed,
                                            logo = transaction.image,
                                            quantity = transaction.quantity,
                                            date = transaction.date,
                                            transaction = transaction.transaction,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp, horizontal = 15.dp)
                                                .graphicsLayer(
                                                    scaleX = scale.value,
                                                    scaleY = scale.value
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }


        if (transactionState.error.isNotBlank()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.offline),
                    contentDescription = "offline",
                    modifier = Modifier
                        .size(200.dp) // Ensure image is smaller than the border container
                        .padding(6.dp)
                        .background(Color.Transparent),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = transactionState.error,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
        }

        if (transactionState.isLoading) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp)
            )
        }

        /*Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 15.dp,bottom=25.dp), // Ensures text doesn't shift
            contentAlignment = Alignment.BottomEnd

        ) {
            AnimatedFab(onFilterClicked = {index->
                isFilterClicked=index
            })
        }*/
    }
}
