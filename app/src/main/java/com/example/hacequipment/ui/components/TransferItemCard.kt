package com.example.hacequipment.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.Transfer
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
fun TransferItemCard(
    transfer: Transfer,
    isAdmin: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("transfer_item_${transfer.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = HacSurface),
        border = BorderStroke(1.dp, HacBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Equipment Name and Monospace Code
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = transfer.equipmentName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = HacText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = transfer.code,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HacAccentAmber
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Route: [From] -> [To]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = transfer.fromProject.ifBlank { "Unassigned" },
                        fontSize = 13.sp,
                        color = HacTextMuted
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = "to",
                        tint = HacAccentAmber,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(14.dp)
                    )
                    Text(
                        text = transfer.toProject,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = HacGreen
                    )
                }

                // Requester
                if (transfer.requestedBy.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Requester",
                            tint = HacBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Requested By: ${transfer.requestedBy}",
                            fontSize = 12.sp,
                            color = HacBlue
                        )
                    }
                }

                // Attachment
                if (transfer.attachmentUrl.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(transfer.attachmentUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .testTag("transfer_attachment_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AttachFile,
                            contentDescription = "Attachment",
                            tint = HacAccentAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Open Attachment",
                            fontSize = 12.sp,
                            color = HacAccentAmber,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Notes
                if (transfer.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Notes: ${transfer.notes}",
                        fontSize = 11.sp,
                        color = HacTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Date and logged by
                Text(
                    text = "${transfer.date} · by ${transfer.byUser}",
                    fontSize = 11.sp,
                    color = HacTextMuted
                )
            }

            if (isAdmin) {
                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.testTag("delete_transfer_btn_${transfer.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete transfer",
                        tint = HacRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Transfer Record", fontWeight = FontWeight.Bold, color = HacText) },
            text = {
                Text(
                    "Deleting this transfer will revert ${transfer.equipmentName} (${transfer.code}) back to '${transfer.fromProject}'. Continue?",
                    color = HacTextMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    },
                    modifier = Modifier.testTag("confirm_delete_transfer_btn")
                ) {
                    Text("Revert & Delete", color = HacRed, fontWeight = FontWeight.Bold)
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
