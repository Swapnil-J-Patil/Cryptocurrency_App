package com.example.cleanarchitectureproject.presentation.auth_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.common_components.OrDivider

@Composable
fun AuthCard(
    firstText: String,
    secondText: String,
    buttonText: String,
    color: Color,
    isSignIn: Boolean? = false,
    onAuthClick: (String, String,String,String) -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
            shape = RoundedCornerShape(8.dp),
            label = { Text(firstText) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "login icon",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }, // Set leading text instead of icon

            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
            shape = RoundedCornerShape(8.dp),
            label = { Text(secondText) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "login icon",
                    modifier = Modifier.padding(end = 10.dp)
                )
            },
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(color = color, RoundedCornerShape(8.dp))
                .border(1.dp, color, RoundedCornerShape(8.dp)),
            onClick = {
                if (isSignIn == true) {
                    onAuthClick("signIn", "email",email.value,password.value)
                } else {
                    onAuthClick("signUp", "email",email.value,password.value)
                }
            },
            contentPadding = PaddingValues(vertical = 10.dp),
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OrDivider()
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
        ) {

            if (isSignIn == true) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .border(2.dp, Color(0xFF23af92), CircleShape) // Apply border first
                        .clip(CircleShape)
                        .clickable {
                            onAuthClick("signIn", "gmail","","")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.googlelogo),
                        contentDescription = "google",
                        modifier = Modifier
                            .size(40.dp) // Ensure image is smaller than the border container
                            .clip(CircleShape)
                            .background(Color.Transparent),
                        contentScale = ContentScale.Inside
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .border(2.dp, Color(0xFF23af92), CircleShape) // Apply border first
                    .clip(CircleShape)
                    .clickable {
                        if(isSignIn==true)
                        {
                            onAuthClick("signIn","fingerprint","","")
                        }
                        else
                        {
                            onAuthClick("signUp","fingerprint","","")
                        }
                    }
                , // Clip after border
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fingerprint),
                    contentDescription = "fingerprint",
                    modifier = Modifier
                        .size(50.dp) // Ensure image is smaller than the border container
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    contentScale = ContentScale.Inside
                )
            }

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .border(2.dp, Color(0xFF23af92), CircleShape) // Apply border first
                    .clip(CircleShape)
                    .clickable {
                        if(isSignIn==true)
                        {
                            onAuthClick("signIn","faceId","","")
                        }
                        else
                        {
                            onAuthClick("signUp","faceId","","")
                        }
                    }
                , // Clip after border
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.faceid),
                    contentDescription = "faceId",
                    modifier = Modifier
                        .size(50.dp) // Ensure image is smaller than the border container
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    contentScale = ContentScale.Inside
                )
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

    }
}