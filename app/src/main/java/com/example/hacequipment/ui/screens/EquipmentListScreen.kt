package com.example.hacequipment.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.FilterState
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.ui.components.ActiveFiltersBar
import com.example.hacequipment.ui.components.EquipmentItemCard
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun EquipmentListScreen(
    equipmentList: List<Equipment>,
    filterState: FilterState,
    isAdmin: Boolean,
    onSearchChange: (String) -> Unit,
    onCategoryFilter: (String?) -> Unit,
    onOdFilter: (String?) -> Unit,
    onProjectFilter: (String?) -> Unit,
    onClearAllFilters: () -> Unit,
    onAddEquipmentClick: () -> Unit,
    onEquipmentClick: (Equipment) -> Unit,
    onTransferClick: (Equipment) -> Unit,
    onEditClick: (Equipment) -> Unit,
    onDeleteClick: (Equipment) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("equipment_list_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title & Add Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Equipment List",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = HacText
                    )
                    Text(
                        text = "${equipmentList.size} items matching filters",
                        fontSize = 13.sp,
                        color = HacTextMuted
                    )
                }

                if (isAdmin) {
                    Button(
                        onClick = onAddEquipmentClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HacAccentAmber,
                            contentColor = HacSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("add_equipment_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Equipment", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = filterState.searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search by code, brand, SAP, project, or model...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = HacTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (filterState.searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Clear search",
                                tint = HacTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("equipment_search_input"),
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

        // Category Filter Chips Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("category_filter_chips_row")
            ) {
                item {
                    val isAll = filterState.category == null
                    Box(
                        modifier = Modifier
                            .background(
                                if (isAll) HacAccentAmber.copy(alpha = 0.2f) else HacSurface,
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                1.dp,
                                if (isAll) HacAccentAmber else HacBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onCategoryFilter(null) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("cat_chip_all")
                    ) {
                        Text(
                            text = "All Categories",
                            fontSize = 12.sp,
                            fontWeight = if (isAll) FontWeight.Bold else FontWeight.Normal,
                            color = if (isAll) HacAccentAmber else HacText
                        )
                    }
                }

                items(OdConfig.CATEGORIES) { cat ->
                    val isSelected = filterState.category.equals(cat, ignoreCase = true)
                    val catColor = OdConfig.getCategoryColor(cat)
                    val displayName = cat.replace(" Equipment", "")

                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) catColor.copy(alpha = 0.2f) else HacSurface,
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) catColor else HacBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onCategoryFilter(cat) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("cat_chip_${displayName.lowercase()}")
                    ) {
                        Text(
                            text = displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) catColor else HacText
                        )
                    }
                }
            }
        }

        // Active Filters Bar
        item {
            ActiveFiltersBar(
                filterState = filterState,
                onRemoveCategory = { onCategoryFilter(null) },
                onRemoveOd = { onOdFilter(null) },
                onRemoveProject = { onProjectFilter(null) },
                onClearAll = onClearAllFilters
            )
        }

        // Equipment List items
        if (equipmentList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No equipment found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HacText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search query or filters.",
                            fontSize = 12.sp,
                            color = HacTextMuted
                        )
                    }
                }
            }
        } else {
            items(equipmentList, key = { it.id }) { eq ->
                EquipmentItemCard(
                    equipment = eq,
                    isAdmin = isAdmin,
                    onClick = { onEquipmentClick(eq) },
                    onTransferClick = { onTransferClick(eq) },
                    onEditClick = { onEditClick(eq) },
                    onDeleteClick = { onDeleteClick(eq) }
                )
            }
        }
    }
}
