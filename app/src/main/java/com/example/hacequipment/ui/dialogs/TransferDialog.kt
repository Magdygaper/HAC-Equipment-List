package com.example.hacequipment.ui.dialogs

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBlue
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacGreen
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferDialog(
    equipment: Equipment,
    allProjects: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (toProject: String, date: String, requestedBy: String, notes: String, attachmentUrl: String) -> Unit
) {
    var toProject by remember { mutableStateOf("") }
    var projectSearch by remember { mutableStateOf("") }
    var projectExpanded by remember { mutableStateOf(false) }

    val todayDate = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    var transferDate by remember { mutableStateOf(todayDate) }
    var requestedBy by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var attachmentUrl by remember { mutableStateOf("") }

    val autoOd by remember {
        derivedStateOf { OdConfig.getOdForProject(toProject) }
    }

    val filteredProjects = remember(projectSearch, allProjects) {
        if (projectSearch.isBlank()) allProjects
        else allProjects.filter { it.contains(projectSearch, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("transfer_dialog"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = HacSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, HacBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Title
                Text(
                    text = "Transfer Equipment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = HacText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${equipment.equipment} ",
                        fontSize = 13.sp,
                        color = HacTextMuted
                    )
                    Text(
                        text = equipment.code,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = HacAccentAmber
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Current Project (From)
                OutlinedTextField(
                    value = equipment.project.ifBlank { "Unassigned" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("FROM PROJECT") },
                    modifier = Modifier.fillMaxWidth().testTag("transfer_from_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HacBorder,
                        unfocusedBorderColor = HacBorder,
                        focusedLabelColor = HacTextMuted,
                        unfocusedLabelColor = HacTextMuted,
                        focusedTextColor = HacTextMuted,
                        unfocusedTextColor = HacTextMuted
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Destination Project (To) with dropdown / search
                ExposedDropdownMenuBox(
                    expanded = projectExpanded,
                    onExpandedChange = { projectExpanded = !projectExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = toProject,
                        onValueChange = {
                            toProject = it
                            projectSearch = it
                            projectExpanded = true
                        },
                        label = { Text("TO PROJECT *") },
                        placeholder = { Text("Search or type destination site...") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projectExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("transfer_to_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = projectExpanded,
                        onDismissRequest = { projectExpanded = false },
                        modifier = Modifier
                            .background(HacSurfaceElevated)
                            .heightIn(max = 240.dp)
                    ) {
                        filteredProjects.take(20).forEach { proj ->
                            val od = OdConfig.getOdForProject(proj)
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(proj, color = HacText, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        if (od.isNotBlank()) {
                                            Text(od, color = HacTextMuted, fontSize = 11.sp)
                                        }
                                    }
                                },
                                onClick = {
                                    toProject = proj
                                    projectSearch = proj
                                    projectExpanded = false
                                },
                                modifier = Modifier.testTag("proj_item_$proj")
                            )
                        }
                    }
                }

                // O.D Engineer preview
                if (autoOd.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Assigned O.D: $autoOd",
                        fontSize = 11.sp,
                        color = HacBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date
                OutlinedTextField(
                    value = transferDate,
                    onValueChange = { transferDate = it },
                    label = { Text("TRANSFER DATE (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth().testTag("transfer_date_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HacAccentAmber,
                        unfocusedBorderColor = HacBorder,
                        focusedLabelColor = HacAccentAmber,
                        unfocusedLabelColor = HacTextMuted,
                        focusedTextColor = HacText,
                        unfocusedTextColor = HacText
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Requested By
                OutlinedTextField(
                    value = requestedBy,
                    onValueChange = { requestedBy = it },
                    label = { Text("REQUESTED BY") },
                    placeholder = { Text("e.g. Eng. Hamdy, Site Engineer") },
                    modifier = Modifier.fillMaxWidth().testTag("transfer_requested_by_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HacAccentAmber,
                        unfocusedBorderColor = HacBorder,
                        focusedLabelColor = HacAccentAmber,
                        unfocusedLabelColor = HacTextMuted,
                        focusedTextColor = HacText,
                        unfocusedTextColor = HacText
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Attachment URL
                OutlinedTextField(
                    value = attachmentUrl,
                    onValueChange = { attachmentUrl = it },
                    label = { Text("ATTACHMENT URL (OneDrive / Cloudinary / PDF)") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth().testTag("transfer_attachment_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HacAccentAmber,
                        unfocusedBorderColor = HacBorder,
                        focusedLabelColor = HacAccentAmber,
                        unfocusedLabelColor = HacTextMuted,
                        focusedTextColor = HacText,
                        unfocusedTextColor = HacText
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("NOTES") },
                    placeholder = { Text("Reason for transfer, gate pass info...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth().testTag("transfer_notes_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HacAccentAmber,
                        unfocusedBorderColor = HacBorder,
                        focusedLabelColor = HacAccentAmber,
                        unfocusedLabelColor = HacTextMuted,
                        focusedTextColor = HacText,
                        unfocusedTextColor = HacText
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HacBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", color = HacTextMuted)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (toProject.isNotBlank()) {
                                onConfirm(toProject.trim(), transferDate.trim(), requestedBy.trim(), notes.trim(), attachmentUrl.trim())
                            }
                        },
                        enabled = toProject.isNotBlank(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HacAccentAmber,
                            contentColor = HacSurface
                        ),
                        modifier = Modifier.testTag("transfer_submit_btn")
                    ) {
                        Text("Save Transfer", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
