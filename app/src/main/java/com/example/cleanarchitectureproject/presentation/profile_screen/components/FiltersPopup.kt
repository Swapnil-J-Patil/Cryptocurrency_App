package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.common_components.SquishyToggleSwitch
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightBackground

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
