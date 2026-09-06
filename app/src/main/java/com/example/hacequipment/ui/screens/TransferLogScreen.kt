package com.example.hacequipment.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.Transfer
import com.example.hacequipment.ui.components.TransferItemCard
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun TransferLogScreen(
    transfers: List<Transfer>,
    isAdmin: Boolean,
    onDeleteTransfer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredTransfers = remember(transfers, searchQuery) {
        if (searchQuery.isBlank()) transfers
        else {
            val q = searchQuery.trim().lowercase()
            transfers.filter {
                it.code.lowercase().contains(q) ||
                        it.equipmentName.lowercase().contains(q) ||
                        it.fromProject.lowercase().contains(q) ||
                        it.toProject.lowercase().contains(q) ||
                        it.requestedBy.lowercase().contains(q)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("transfer_log_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Transfer Log",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = HacText
                )
                Text(
                    text = "Full movement history of equipment between construction sites",
                    fontSize = 13.sp,
                    color = HacTextMuted
                )
            }
        }

        // Search
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by code, equipment, or project site...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = HacTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Clear",
                                tint = HacTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("transfers_search_input"),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HacAccentAmber,
                    unfocusedBorderColor = HacBorder,
                    focusedTextColor = HacText,
                    unfocusedTextColor = HacText,
                    focusedContainerColor = HacSurface,
                    unfocusedContainerColor = HacSurface
                )
            )
        }

        if (filteredTransfers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No transfers found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HacText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No equipment transfers match the criteria.",
                            fontSize = 12.sp,
                            color = HacTextMuted
                        )
                    }
                }
            }
        } else {
            items(filteredTransfers, key = { it.id }) { tr ->
                TransferItemCard(
                    transfer = tr,
                    isAdmin = isAdmin,
                    onDeleteClick = { onDeleteTransfer(tr.id) }
                )
            }
        }
    }
}
