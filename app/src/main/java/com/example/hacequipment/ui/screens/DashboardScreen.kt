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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.FilterState
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.data.Transfer
import com.example.hacequipment.ui.components.ActiveFiltersBar
import com.example.hacequipment.ui.components.CategoryCard
import com.example.hacequipment.ui.components.KpiCard
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBlue
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacGreen
import com.example.hacequipment.ui.theme.HacPurple
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun DashboardScreen(
    equipmentList: List<Equipment>,
    transfers: List<Transfer>,
    filterState: FilterState,
    onCategoryFilter: (String?) -> Unit,
    onOdFilter: (String?) -> Unit,
    onProjectFilter: (String?) -> Unit,
    onClearAllFilters: () -> Unit,
    onBreakdownClick: (String) -> Unit,
    onNavigateToEquipment: () -> Unit,
    modifier: Modifier = Modifier
) {
    var projectSearchText by remember { mutableStateOf("") }

    // KPI Calculations
    val totalEquipmentCount = equipmentList.size
    val activeProjectsCount = remember(equipmentList) {
        equipmentList.map { it.project }.filter { it.isNotBlank() }.distinct().size
    }
    val odEngineersCount = remember(equipmentList) {
        equipmentList.map { it.od }.filter { it.isNotBlank() }.distinct().size
    }
    val lastTransferDate = remember(transfers) {
        transfers.firstOrNull()?.date ?: "2026-09-05"
    }

    // Category distribution counts based on current OD + Project filters
    val categoryCounts = remember(equipmentList, filterState.od, filterState.project) {
        val filtered = equipmentList.filter { eq ->
            val matchOd = filterState.od == null || eq.od.equals(filterState.od, ignoreCase = true)
            val matchProj = filterState.project == null || eq.project.equals(filterState.project, ignoreCase = true)
            matchOd && matchProj
        }
        val map = mutableMapOf<String, Int>()
        OdConfig.CATEGORIES.forEach { cat -> map[cat] = 0 }
        filtered.forEach { eq ->
            map[eq.category] = (map[eq.category] ?: 0) + 1
        }
        map.entries.sortedByDescending { it.value }
    }

    // Distinct O.D Engineers list
    val allOds = remember(equipmentList) {
        val fromDb = equipmentList.map { it.od }.filter { it.isNotBlank() }
        val fromMap = OdConfig.OD_MAP.values
        (fromDb + fromMap).distinct().sorted()
    }

    // Project counts
    val projectList = remember(equipmentList, projectSearchText) {
        val counts = mutableMapOf<String, Int>()
        equipmentList.forEach { eq ->
            if (eq.project.isNotBlank()) {
                counts[eq.project] = (counts[eq.project] ?: 0) + 1
            }
        }
        val sorted = counts.entries.sortedByDescending { it.value }
        if (projectSearchText.isBlank()) sorted
        else sorted.filter { it.key.contains(projectSearchText, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Header
        item {
            Column {
                Text(
                    text = "Dashboard",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = HacText
                )
                Text(
                    text = "Real-time overview of fleet allocation and transfers",
                    fontSize = 13.sp,
                    color = HacTextMuted
                )
            }
        }

        // Section: 4 KPI Cards (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Total Equipment",
                        value = "$totalEquipmentCount",
                        subtitle = "across all sites",
                        accentColor = HacAccentAmber,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_total_equipment",
                        onClick = onNavigateToEquipment
                    )
                    KpiCard(
                        title = "Active Projects",
                        value = "$activeProjectsCount",
                        subtitle = "construction sites",
                        accentColor = HacBlue,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_active_projects"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "O.D Engineers",
                        value = "$odEngineersCount",
                        subtitle = "operations directors",
                        accentColor = HacGreen,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_od_engineers"
                    )
                    KpiCard(
                        title = "Transfers Logged",
                        value = "${transfers.size}",
                        subtitle = "last on $lastTransferDate",
                        accentColor = HacPurple,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_transfers"
                    )
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

        // Section: By Category
        item {
            Column {
                Text(
                    text = "BY CATEGORY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = HacTextMuted
                )
                Text(
                    text = "Tap to filter equipment · ℹ for type breakdown",
                    fontSize = 11.sp,
                    color = HacTextMuted
                )
            }
        }

        // Category Cards Grid (2 per row)
        items(categoryCounts.chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val first = pair[0]
                CategoryCard(
                    categoryName = first.key,
                    count = first.value,
                    accentColor = OdConfig.getCategoryColor(first.key),
                    isActive = filterState.category.equals(first.key, ignoreCase = true),
                    onFilterClick = { onCategoryFilter(first.key) },
                    onBreakdownClick = { onBreakdownClick(first.key) },
                    modifier = Modifier.weight(1f)
                )

                if (pair.size > 1) {
                    val second = pair[1]
                    CategoryCard(
                        categoryName = second.key,
                        count = second.value,
                        accentColor = OdConfig.getCategoryColor(second.key),
                        isActive = filterState.category.equals(second.key, ignoreCase = true),
                        onFilterClick = { onCategoryFilter(second.key) },
                        onBreakdownClick = { onBreakdownClick(second.key) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Section: By O.D Engineer
        item {
            Column {
                Text(
                    text = "BY O.D ENGINEER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = HacTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("od_filter_row")
                ) {
                    items(allOds) { odName ->
                        val isSelected = filterState.od.equals(odName, ignoreCase = true)
                        val chipBg = if (isSelected) HacBlue.copy(alpha = 0.2f) else HacSurface
                        val chipBorder = if (isSelected) HacBlue else HacBorder
                        val chipText = if (isSelected) HacBlue else HacText

                        Box(
                            modifier = Modifier
                                .background(chipBg, RoundedCornerShape(16.dp))
                                .border(1.dp, chipBorder, RoundedCornerShape(16.dp))
                                .clickable { onOdFilter(odName) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("od_chip_$odName")
                        ) {
                            Text(
                                text = odName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = chipText
                            )
                        }
                    }
                }
            }
        }

        // Section: By Project
        item {
            Column {
                Text(
                    text = "BY PROJECT / SITE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = HacTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = projectSearchText,
                    onValueChange = { projectSearchText = it },
                    placeholder = { Text("Search project...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = HacTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("project_search_input"),
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
        }

        // Project Rows
        items(projectList) { entry ->
            val projName = entry.key
            val count = entry.value
            val assignedOd = OdConfig.getOdForProject(projName).ifBlank { "Assigned Engineer" }
            val isSelected = filterState.project.equals(projName, ignoreCase = true)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) HacSurfaceElevated else HacSurface)
                    .border(
                        1.dp,
                        if (isSelected) HacGreen else HacBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onProjectFilter(projName) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .testTag("project_row_$projName"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(if (isSelected) HacGreen else HacAccentAmber, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = projName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HacText,
                            maxLines = 1
                        )
                        Text(
                            text = assignedOd,
                            fontSize = 11.sp,
                            color = HacTextMuted,
                            maxLines = 1
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(HacSurfaceElevated, RoundedCornerShape(12.dp))
                        .border(1.dp, HacBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$count",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) HacGreen else HacAccentAmber
                    )
                }
            }
        }
    }
}
