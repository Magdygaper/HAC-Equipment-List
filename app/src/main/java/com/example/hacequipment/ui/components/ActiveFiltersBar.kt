package com.example.hacequipment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.FilterState
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBlue
import com.example.hacequipment.ui.theme.HacGreen
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun ActiveFiltersBar(
    filterState: FilterState,
    onRemoveCategory: () -> Unit,
    onRemoveOd: () -> Unit,
    onRemoveProject: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasFilters = filterState.category != null || filterState.od != null || filterState.project != null

    if (!hasFilters) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_filters_bar")
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "FILTERS:",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = HacTextMuted
        )

        filterState.category?.let { cat ->
            FilterChip(
                label = cat.replace(" Equipment", ""),
                accentColor = HacAccentAmber,
                onDismiss = onRemoveCategory,
                testTag = "chip_remove_cat"
            )
        }

        filterState.od?.let { od ->
            FilterChip(
                label = od,
                accentColor = HacBlue,
                onDismiss = onRemoveOd,
                testTag = "chip_remove_od"
            )
        }

        filterState.project?.let { proj ->
            FilterChip(
                label = proj,
                accentColor = HacGreen,
                onDismiss = onRemoveProject,
                testTag = "chip_remove_proj"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Clear all",
            fontSize = 11.sp,
            color = HacTextMuted,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable { onClearAll() }
                .testTag("clear_all_filters_btn")
                .padding(4.dp)
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    accentColor: Color,
    onDismiss: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable { onDismiss() }
            .testTag(testTag)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = accentColor,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove filter",
            tint = accentColor,
            modifier = Modifier.size(12.dp)
        )
    }
}
