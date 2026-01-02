package com.swapnil.dreamtrade.presentation.common_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Divider(
    isBuy: Boolean?=true,
    text: String ="Or",
    padding: Dp =16.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = padding)
            .offset(y=if (isBuy==false) -20.dp else 0.dp)
    ) {
        androidx.compose.material.Divider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            thickness = 2.dp
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        androidx.compose.material.Divider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            thickness = 2.dp
        )
    }
}
