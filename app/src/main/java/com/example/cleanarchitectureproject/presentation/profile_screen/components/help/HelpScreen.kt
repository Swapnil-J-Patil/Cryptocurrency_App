package com.example.cleanarchitectureproject.presentation.profile_screen.components.help

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue //
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.model.HelpItemData
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.grey
import com.example.cleanarchitectureproject.presentation.ui.theme.lightGrey
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch


@Composable
fun HelpScreen() {
    val helpItemData = listOf(
        HelpItemData(
            question = "🔐  How do I log in?",
            answers = listOf(
                "Email & password",
                "Google Sign-In",
                "Fingerprint or Pattern Lock (for quick access)"
            )
        ),
        HelpItemData(
            question = "💵  What is Paper Money?",
            answers = listOf(
                "DreamTrade gives you virtual USD to simulate real trades.",
                "This isn’t real money, so you can learn and experiment with no risk."
            )
        ),
        HelpItemData(
            question = "📈  How does trading work?",
            answers = listOf(
                "1. Go to the Market screen",
                "2. Search and select a coin",
                "3. Tap Buy or Sell",
                "4. Use the slider or enter amount manually",
                "5. Confirm the transaction"
            )
        ),
        HelpItemData(
            question = "💼  What’s in my Portfolio?",
            answers = listOf(
                "All your purchased coins",
                "A donut chart showing top holdings",
                "Real-time value updates based on market price"
            )
        ),
        HelpItemData(
            question = "🎡  What is Lucky Wheel?",
            answers = listOf(
                "Spin the Lucky Wheel every 3 hours to earn bonus paper money 💰",
                "Use it to boost your virtual wallet and trade more!"
            )
        ),
        HelpItemData(
            question = "🌙  How to switch to Dark Mode?",
            answers = listOf(
                "Go to Settings and toggle between Light and Dark Mode."
            )
        ),
        HelpItemData(
            question = "🧑‍💼  Who built DreamTrade?",
            answers = listOf(
                "DreamTrade is a Proof of Concept, built by a solo developer — Swapnil Patil.",
                "One vision. One codebase. Many late nights.",
                "Just how Harvey Specter would approve. 😉"
            )
        ),
        HelpItemData(
            question = "📩  Need help?",
            answers = listOf(
                "Got feedback or questions?",
                "Reach out to: swapnil.androiddev@gmail.com"
            )
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                    center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                    radius = 1500f // Adjust based on screen size
                )
            ),
        contentAlignment = Alignment.TopCenter
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
                text = "FAQ's",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = Poppins,
                style = MaterialTheme.typography.displaySmall,
            )
            //Spacer(modifier = Modifier.width(15.dp))
        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, bottom = 20.dp, start = 8.dp, end = 8.dp),
            //.animateContentSize(), // Card grows/shrinks with content
            colors = CardDefaults.cardColors(containerColor = grey),
            border = BorderStroke(2.dp, lightGrey),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .animateContentSize() // animate height changes when content changes
            ) {
                helpItemData.forEach { item ->
                    ExpandableHelpItem(item)
                }
            }
        }


    }
}


