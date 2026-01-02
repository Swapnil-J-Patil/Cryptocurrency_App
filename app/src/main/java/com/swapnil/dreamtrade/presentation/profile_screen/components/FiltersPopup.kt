package com.swapnil.dreamtrade.presentation.profile_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swapnil.dreamtrade.presentation.common_components.SquishyToggleSwitch
import com.swapnil.dreamtrade.presentation.ui.theme.green

@Composable
fun FiltersPopup(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    filter: String,
    isTab: Boolean,
    currentFilters: MutableState<List<Boolean>>, // MutableState for filters
    onFilters: (List<Boolean>) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onCancel() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sort by",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Current value",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        SquishyToggleSwitch(green,
                            isTurnedOn = currentFilters.value[0], // Using currentFilters.value
                            onTurnedOn = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[0] = true
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            },
                            onTurnedOff = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[0] = false
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            }
                        ) // Green
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Returns",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        SquishyToggleSwitch(green,
                            isTurnedOn = currentFilters.value[1],
                            onTurnedOn = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[1] = true
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            },
                            onTurnedOff = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[1] = false
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            }
                        ) // Green
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PercentChange",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        SquishyToggleSwitch(green,
                            isTurnedOn = currentFilters.value[2],
                            onTurnedOn = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[2] = true
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            },
                            onTurnedOff = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[2] = false
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            }
                        ) // Green
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Coin Name",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        SquishyToggleSwitch(green,
                            isTurnedOn = currentFilters.value[3],
                            onTurnedOn = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[3] = true
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            },
                            onTurnedOff = {
                                val updatedFilters = currentFilters.value.toMutableList()
                                updatedFilters[3] = false
                                currentFilters.value = updatedFilters
                                onFilters(updatedFilters)
                            }
                        ) // Green
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
