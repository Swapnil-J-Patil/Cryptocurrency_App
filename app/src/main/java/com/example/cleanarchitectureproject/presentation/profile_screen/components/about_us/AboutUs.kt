package com.example.cleanarchitectureproject.presentation.profile_screen.components.about_us

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.model.AboutUsData
import com.example.cleanarchitectureproject.presentation.profile_screen.components.lucky_wheel.LuckyWheel
import com.example.cleanarchitectureproject.presentation.profile_screen.components.lucky_wheel.WheelStand
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.lightBackground
import com.example.cleanarchitectureproject.presentation.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AboutUs(modifier: Modifier = Modifier) {
    val priorities = remember { mutableStateListOf(1f, 0.95f, 0.9f, 0.85f) }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isTab = configuration.screenWidthDp >= 600 && configuration.screenHeightDp >= 600
    val aboutUsData = remember {
        mutableStateListOf(
            AboutUsData(
                title = "What's our Vision?",
                imageUrl = "https://www.lntsapura.com/images/vision.jpg",
                text = listOf(
                    "Our mission is to remove the fear of financial risk while giving users the freedom to explore, experiment, and grow in the world of crypto trading.",
                    "To make crypto trading education accessible and engaging for everyone."
                )
            ),
            AboutUsData(
                title = "How It Works?",
                imageUrl = "https://static.vecteezy.com/system/resources/previews/011/602/940/non_2x/how-it-works-text-button-how-it-works-sign-speech-bubble-web-banner-label-template-illustration-vector.jpg",
                text = listOf(
                    "Start with Free Virtual Money – Get a paper wallet preloaded with USD.",
                    "Explore Real Market Data – Prices update live, just like real crypto exchanges. Track Your Growth – View your portfolio, gains/losses, and trading history."
                )
            ),
            AboutUsData(
                title = "What we Offer?",
                imageUrl = "https://i.ibb.co/gbqfrLRc/what-we-offer.png",
                text = listOf(
                    "Trade real cryptocurrencies using virtual (paper) money, Experience live market prices and realistic trading dynamics.",
                    "Learn and improve your trading skills in a risk-free environment."
                )
            ),
            AboutUsData(
                title = "Why Choose Us?",
                imageUrl = "https://static.vecteezy.com/system/resources/previews/016/623/366/non_2x/why-choose-us-text-message-banner-design-flat-illustration-vector.jpg",
                text = listOf(
                    "We believe in learning by doing. DreamTrade empowers you to experiment, fail, and grow — without losing a single rupee.",
                    "Whether you're just starting out or sharpening your skills."
                )
            ),
        )
    }

    val aboutMe =
        "DreamTrade is a POC — a one-man mission led by me, Swapnil Patil. No big team. No noise. Just vision, code, and execution."

    if (isTab && !isPortrait) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // Optional, if you want full height
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Splash Image",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    fontFamily = Poppins,
                )

            }
            Spacer(
                Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                            center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                            radius = 1500f // Adjust based on screen size
                        )
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(top = 50.dp, start = 16.dp, end = 8.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Currency Logo (10%)
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                    )

                    Spacer(modifier = Modifier.width(15.dp))
                    // Price and Percentage (20%)
                    Text(
                        text = "About Us",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontFamily = Poppins,
                        style = MaterialTheme.typography.displaySmall,
                    )
                    //Spacer(modifier = Modifier.width(15.dp))
                }
                Text(
                    text = aboutMe,
                    modifier = Modifier.padding(start = 18.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Poppins,
                    color = white.copy(alpha = 0.8f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    for (i in 3 downTo 0) {
                        AboutUsCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f)
                                .padding(bottom = 20.dp),
                            data = aboutUsData[i],
                            priority = priorities[i],
                            index = i,
                            onMoveToBack = {
                                priorities.add(priorities.removeAt(0))
                                aboutUsData.add(aboutUsData.removeAt(0))
                            }
                        )
                    }

                }
            }
        }
    }
    else if (!isTab && isPortrait) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                        center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                        radius = 1500f // Adjust based on screen size
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(top = 50.dp, start = 16.dp, end = 8.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Currency Logo (10%)
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )

                Spacer(modifier = Modifier.width(15.dp))
                // Price and Percentage (20%)
                Text(
                    text = "About Us",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins,
                    style = MaterialTheme.typography.displaySmall,
                )
                //Spacer(modifier = Modifier.width(15.dp))
            }
            Text(
                text = aboutMe,
                modifier = Modifier.padding(start = 18.dp, end = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = Poppins,
                color = white.copy(alpha = 0.8f)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                for (i in 3 downTo 0) {
                    AboutUsCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                            .padding(bottom = 20.dp),
                        data = aboutUsData[i],
                        priority = priorities[i],
                        index = i,
                        onMoveToBack = {
                            priorities.add(priorities.removeAt(0))
                            aboutUsData.add(aboutUsData.removeAt(0))
                        }
                    )
                }

            }
        }
    }
    else if (isTab && isPortrait) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // Optional, if you want full height
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Splash Image",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    fontFamily = Poppins,
                )

            }
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                            center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                            radius = 1500f // Adjust based on screen size
                        )
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(top = 50.dp, start = 16.dp, end = 8.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Currency Logo (10%)
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                    )

                    Spacer(modifier = Modifier.width(15.dp))
                    // Price and Percentage (20%)
                    Text(
                        text = "About Us",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontFamily = Poppins,
                        style = MaterialTheme.typography.displaySmall,
                    )
                    //Spacer(modifier = Modifier.width(15.dp))
                }
                Text(
                    text = aboutMe,
                    modifier = Modifier.padding(start = 18.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Poppins,
                    color = white.copy(alpha = 0.8f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    for (i in 3 downTo 0) {
                        AboutUsCardLandscape(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.7f)
                                .padding(bottom = 20.dp),
                            data = aboutUsData[i],
                            priority = priorities[i],
                            index = i,
                            onMoveToBack = {
                                priorities.add(priorities.removeAt(0))
                                aboutUsData.add(aboutUsData.removeAt(0))
                            }
                        )
                    }

                }
            }
        }
    }
    else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                        center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                        radius = 1500f // Adjust based on screen size
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(top = 50.dp, start = 16.dp, end = 8.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Currency Logo (10%)
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )

                Spacer(modifier = Modifier.width(15.dp))
                // Price and Percentage (20%)
                Text(
                    text = "About Us",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins,
                    style = MaterialTheme.typography.displaySmall,
                )
                //Spacer(modifier = Modifier.width(15.dp))
            }
            Text(
                text = aboutMe,
                modifier = Modifier.padding(start = 18.dp, end = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = Poppins,
                color = white.copy(alpha = 0.8f)
            )*/
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                for (i in 3 downTo 0) {
                    AboutUsCardLandscape(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.9f),
                        data = aboutUsData[i],
                        priority = priorities[i],
                        index = i,
                        onMoveToBack = {
                            priorities.add(priorities.removeAt(0))
                            aboutUsData.add(aboutUsData.removeAt(0))
                        }
                    )
                }

            }
        }

    }
}

