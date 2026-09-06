package com.example.hacequipment.ui.dialogs

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.data.Transfer
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBlue
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacGreen
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

@Composable
fun EquipmentHistoryDialog(
    equipment: Equipment,
    transfers: List<Transfer>,
    isAdmin: Boolean,
    onDismiss: () -> Unit,
    onTransferClick: (Equipment) -> Unit
) {
    val context = LocalContext.current
    val catColor = OdConfig.getCategoryColor(equipment.category)
    val equipmentTransfers = transfers.filter { it.equipmentId == equipment.id || (it.code.isNotBlank() && it.code.equals(equipment.code, ignoreCase = true)) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("equipment_history_dialog"),
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
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = equipment.equipment,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = HacText
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = equipment.code,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = HacAccentAmber
                            )
                            if (equipment.brand.isNotBlank()) {
                                Text(
                                    text = " · ${equipment.brand}",
                                    fontSize = 12.sp,
                                    color = HacTextMuted
                                )
                            }
                        }
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

                // Specs Summary Table
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HacSurfaceElevated, RoundedCornerShape(8.dp))
                        .border(1.dp, HacBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SpecRow("Category", equipment.category)
                        SpecRow("Current Project", equipment.project.ifBlank { "Unassigned" })
                        SpecRow("O.D Engineer", equipment.od.ifBlank { "Unassigned" })
                        if (equipment.year.isNotBlank()) SpecRow("Year", equipment.year)
                        if (equipment.power.isNotBlank()) SpecRow("Power/Capacity", equipment.power)
                        if (equipment.sap.isNotBlank()) SpecRow("SAP Code", equipment.sap)
                        if (equipment.chassis.isNotBlank()) SpecRow("Chassis No.", equipment.chassis)
                        if (equipment.engine.isNotBlank()) SpecRow("Engine Model", equipment.engine)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Timeline Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "Transfer History",
                            tint = HacAccentAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Transfer History (${equipmentTransfers.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = HacText
                        )
                    }

                    if (isAdmin) {
                        Button(
                            onClick = {
                                onDismiss()
                                onTransferClick(equipment)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HacGreen,
                                contentColor = HacSurface
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp).testTag("dialog_quick_transfer_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SwapHoriz,
                                contentDescription = "Transfer",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Transfer", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (equipmentTransfers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No transfer records found for this equipment.",
                            fontSize = 12.sp,
                            color = HacTextMuted
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        equipmentTransfers.forEachIndexed { index, tr ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Timeline bullet & line
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(20.dp).padding(top = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(HacAccentAmber, CircleShape)
                                    )
                                    if (index < equipmentTransfers.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(40.dp)
                                                .background(HacBorder)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = tr.fromProject.ifBlank { "Initial Site" },
                                            fontSize = 12.sp,
                                            color = HacTextMuted
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                            contentDescription = "to",
                                            tint = HacAccentAmber,
                                            modifier = Modifier.padding(horizontal = 4.dp).size(12.dp)
                                        )
                                        Text(
                                            text = tr.toProject,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = HacGreen
                                        )
                                    }

                                    Text(
                                        text = "${tr.date} ${if (tr.requestedBy.isNotBlank()) "· Req: " + tr.requestedBy else ""}",
                                        fontSize = 11.sp,
                                        color = HacTextMuted
                                    )

                                    if (tr.notes.isNotBlank()) {
                                        Text(
                                            text = tr.notes,
                                            fontSize = 11.sp,
                                            color = HacText
                                        )
                                    }

                                    if (tr.attachmentUrl.isNotBlank()) {
                                        Row(
                                            modifier = Modifier
                                                .clickable {
                                                    try {
                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tr.attachmentUrl))
                                                        context.startActivity(intent)
                                                    } catch (_: Exception) {}
                                                }
                                                .padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.AttachFile,
                                                contentDescription = "Attachment",
                                                tint = HacAccentAmber,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "Attachment",
                                                fontSize = 11.sp,
                                                color = HacAccentAmber
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            color = HacTextMuted,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = HacText,
            fontWeight = FontWeight.Medium
        )
    }
}
