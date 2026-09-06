package com.example.hacequipment.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun CategoryBreakdownDialog(
    category: String,
    allEquipment: List<Equipment>,
    onDismiss: () -> Unit,
    onApplyFilter: (String) -> Unit
) {
    val catColor = OdConfig.getCategoryColor(category)
    val categoryEquipment = remember(category, allEquipment) {
        allEquipment.filter { it.category.equals(category, ignoreCase = true) }
    }
    val totalCount = categoryEquipment.size
    val fleetTotal = allEquipment.size.coerceAtLeast(1)
    val pctOfFleet = (totalCount * 100) / fleetTotal

    // Distribution by Equipment Type
    val typeCounts = remember(categoryEquipment) {
        val map = mutableMapOf<String, Int>()
        categoryEquipment.forEach { eq ->
            map[eq.equipment] = (map[eq.equipment] ?: 0) + 1
        }
        map.entries.sortedByDescending { it.value }
    }

    // Distribution by O.D Engineer
    val odCounts = remember(categoryEquipment) {
        val map = mutableMapOf<String, Int>()
        categoryEquipment.forEach { eq ->
            val odName = eq.od.ifBlank { "Unassigned" }
            map[odName] = (map[odName] ?: 0) + 1
        }
        map.entries.sortedByDescending { it.value }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("category_breakdown_dialog"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = HacSurface),
            border = BorderStroke(1.dp, HacBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = category.replace(" Equipment", ""),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = HacText
                        )
                        Text(
                            text = "Fleet distribution breakdown",
                            fontSize = 12.sp,
                            color = HacTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = HacTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Big Stat Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HacSurfaceElevated, RoundedCornerShape(8.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CATEGORY TOTAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = HacTextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "$pctOfFleet% of entire fleet",
                            fontSize = 11.sp,
                            color = HacTextMuted
                        )
                    }
                    Text(
                        text = "$totalCount",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = catColor
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 1: Equipment Types
                Text(
                    text = "EQUIPMENT TYPES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = HacTextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val maxTypeCount = typeCounts.firstOrNull()?.value ?: 1
                    typeCounts.forEach { entry ->
                        val ratio = entry.value.toFloat() / maxTypeCount
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = entry.key,
                                    fontSize = 12.sp,
                                    color = HacText,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${entry.value}",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = catColor
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = catColor,
                                trackColor = HacSurfaceElevated
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 2: Distribution by O.D Engineer
                Text(
                    text = "BY O.D ENGINEER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = HacTextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val maxOdCount = odCounts.firstOrNull()?.value ?: 1
                    odCounts.forEach { entry ->
                        val ratio = entry.value.toFloat() / maxOdCount
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = entry.key,
                                    fontSize = 12.sp,
                                    color = HacText,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${entry.value}",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = catColor
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = catColor,
                                trackColor = HacSurfaceElevated
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action: Filter by this category
                Button(
                    onClick = {
                        onDismiss()
                        onApplyFilter(category)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("apply_cat_filter_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = catColor,
                        contentColor = HacSurface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Filter",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Filter Equipment List by this Category", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
