package com.example.cleanarchitectureproject.presentation.profile_screen.components.about_us

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.lightBackground

@Composable
fun AboutUs(modifier: Modifier = Modifier) {
    val priorities = remember { mutableStateListOf(1f, 0.95f, 0.9f, 0.85f) }
    val imageUrl=remember { mutableStateListOf(
        "https://i.ibb.co/gbqfrLRc/what-we-offer.png",
        "https://static.vecteezy.com/system/resources/previews/016/623/366/non_2x/why-choose-us-text-message-banner-design-flat-illustration-vector.jpg",
        "https://www.lntsapura.com/images/vision.jpg",
        "https://static.vecteezy.com/system/resources/previews/011/602/940/non_2x/how-it-works-text-button-how-it-works-sign-speech-bubble-web-banner-label-template-illustration-vector.jpg"
    ) }
    val titles= remember { mutableStateListOf(
        "What we Offer?",
        "Why Choose Us?",
        "What's our Vision?",
        "How It Works?"
    ) }
    val content=remember { mutableStateListOf(
        "Trade real cryptocurrencies using virtual (paper) money, Experience live market prices and realistic trading dynamics. Learn and improve your trading skills in a risk-free environment",
        "We believe in learning by doing. DreamTrade empowers you to experiment, fail, and grow — without losing a single rupee. Whether you're just starting out or sharpening your skills.",
        "Our mission is to remove the fear of financial risk while giving users the freedom to explore, experiment, and grow in the world of crypto trading. To make crypto trading education accessible and engaging for everyone .",
        "Start with Free Virtual Money – Get a paper wallet preloaded with USD. Explore Real Market Data – Prices update live, just like real crypto exchanges. Track Your Growth – View your portfolio, gains/losses, and trading history."
    ) }
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
                text = "DreamTrade",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = Poppins,
                style = MaterialTheme.typography.displaySmall,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            for (i in 3 downTo 0) {
                AboutUsCard(
                    imageUrl = imageUrl[i],
                    text = content[i],
                    priority = priorities[i],
                    index = i,
                    title = titles[i],
                    onMoveToBack = {
                        priorities.add(priorities.removeAt(0))
                        imageUrl.add(imageUrl.removeAt(0))
                        content.add(content.removeAt(0))
                        titles.add(titles.removeAt(0))
                    }
                )
            }

        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun  Preview() {
    AboutUs()
}*/
