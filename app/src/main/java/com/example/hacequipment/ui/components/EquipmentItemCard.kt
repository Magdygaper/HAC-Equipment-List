package com.example.hacequipment.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBlue
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacGreen
import com.example.hacequipment.ui.theme.HacRed
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun EquipmentItemCard(
    equipment: Equipment,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onTransferClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val catColor = OdConfig.getCategoryColor(equipment.category)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("equipment_item_${equipment.code.ifBlank { equipment.id }}")
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = HacSurface),
        border = BorderStroke(1.dp, HacBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Row 1: Header (Code, Category Badge, SN)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = equipment.code.ifBlank { "NO-CODE" },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = HacAccentAmber
                    )
                    if (equipment.year.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(HacSurfaceElevated, RoundedCornerShape(4.dp))
                                .border(1.dp, HacBorder, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = equipment.year,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = HacTextMuted
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .background(catColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .border(1.dp, catColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = equipment.category.replace(" Equipment", ""),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = catColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Equipment type & Brand/Model
            Text(
                text = equipment.equipment,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = HacText
            )
            if (equipment.brand.isNotBlank()) {
                Text(
                    text = equipment.brand,
                    fontSize = 13.sp,
                    color = HacTextMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Project Location & O.D Engineer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Project",
                        tint = HacGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = equipment.project.ifBlank { "Unassigned" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (equipment.project.isNotBlank()) HacText else HacTextMuted,
                        maxLines = 1
                    )
                }

                if (equipment.od.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "O.D",
                            tint = HacBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = equipment.od,
                            fontSize = 11.sp,
                            color = HacTextMuted,
                            maxLines = 1
                        )
                    }
                }
            }

            // Row 4: Action buttons for Admin
            if (isAdmin) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onTransferClick,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("transfer_btn_${equipment.code}"),
                        border = BorderStroke(1.dp, HacGreen),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HacGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SwapHoriz,
                            contentDescription = "Transfer",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Transfer", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("edit_btn_${equipment.code}"),
                        border = BorderStroke(1.dp, HacBlue),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HacBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("delete_btn_${equipment.code}"),
                        border = BorderStroke(1.dp, HacRed),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HacRed)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Equipment", fontWeight = FontWeight.Bold, color = HacText) },
            text = {
                Text(
                    "Are you sure you want to delete ${equipment.equipment} (${equipment.code})? This action cannot be undone.",
                    color = HacTextMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    },
                    modifier = Modifier.testTag("confirm_delete_btn")
                ) {
                    Text("Delete", color = HacRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = HacTextMuted)
                }
            },
            containerColor = HacSurfaceElevated
        )
    }
}
