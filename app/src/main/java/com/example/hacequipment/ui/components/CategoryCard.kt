package com.example.hacequipment.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceHighlight
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun CategoryCard(
    categoryName: String,
    count: Int,
    accentColor: Color,
    isActive: Boolean,
    onFilterClick: () -> Unit,
    onBreakdownClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = categoryName.replace(" Equipment", "")
    val borderColor = if (isActive) accentColor else HacBorder
    val containerColor = if (isActive) HacSurfaceHighlight else HacSurface

    Card(
        modifier = modifier
            .testTag("category_card_${displayName.lowercase()}")
            .clickable { onFilterClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isActive) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$count",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = accentColor
                )
                IconButton(
                    onClick = onBreakdownClick,
                    modifier = Modifier.size(32.dp).testTag("breakdown_btn_${displayName.lowercase()}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "View $displayName Breakdown",
                        tint = HacTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = displayName,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = HacText,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = if (isActive) "Active Filter" else "Filter · Breakdown",
                fontSize = 10.sp,
                color = if (isActive) accentColor else HacTextMuted
            )
        }
    }
}
