package com.example.hacequipment.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBlue
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEquipmentDialog(
    equipment: Equipment?, // null for Add, non-null for Edit
    allProjects: List<String>,
    onDismiss: () -> Unit,
    onSave: (Equipment, isNew: Boolean) -> Unit
) {
    val isNew = equipment == null

    var category by remember { mutableStateOf(equipment?.category ?: OdConfig.CATEGORIES.first()) }
    var catExpanded by remember { mutableStateOf(false) }

    var equipmentType by remember { mutableStateOf(equipment?.equipment ?: "") }
    var brand by remember { mutableStateOf(equipment?.brand ?: "") }
    var code by remember { mutableStateOf(equipment?.code ?: "") }
    var sap by remember { mutableStateOf(equipment?.sap ?: "") }
    var chassis by remember { mutableStateOf(equipment?.chassis ?: "") }
    var engine by remember { mutableStateOf(equipment?.engine ?: "") }
    var year by remember { mutableStateOf(equipment?.year ?: "") }
    var power by remember { mutableStateOf(equipment?.power ?: "") }
    var project by remember { mutableStateOf(equipment?.project ?: "") }
    var projExpanded by remember { mutableStateOf(false) }
    var od by remember { mutableStateOf(equipment?.od ?: "") }
    var costCenter by remember { mutableStateOf(equipment?.costCenter ?: "") }
    var value by remember { mutableStateOf(equipment?.value ?: "") }

    val filteredProjects = remember(project, allProjects) {
        if (project.isBlank()) allProjects
        else allProjects.filter { it.contains(project, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("add_edit_equipment_dialog"),
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
                Text(
                    text = if (isNew) "Add Equipment" else "Edit Equipment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = HacText
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = catExpanded,
                    onExpandedChange = { catExpanded = !catExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("CATEGORY *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("equipment_category_dropdown"),
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
                        expanded = catExpanded,
                        onDismissRequest = { catExpanded = false },
                        modifier = Modifier.background(HacSurfaceElevated)
                    ) {
                        OdConfig.CATEGORIES.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, color = HacText, fontSize = 13.sp) },
                                onClick = {
                                    category = cat
                                    catExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Equipment Type
                OutlinedTextField(
                    value = equipmentType,
                    onValueChange = { equipmentType = it },
                    label = { Text("EQUIPMENT TYPE *") },
                    placeholder = { Text("e.g. Backhoe, Mobile Crane, Generator") },
                    modifier = Modifier.fillMaxWidth().testTag("equipment_type_input"),
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

                // Brand / Model
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("BRAND / MODEL") },
                    placeholder = { Text("e.g. Bobcat B730, CAT 320D") },
                    modifier = Modifier.fillMaxWidth().testTag("equipment_brand_input"),
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

                // Code and Year row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("CODE / BOARD NO. *") },
                        placeholder = { Text("BH0001") },
                        modifier = Modifier.weight(1f).testTag("equipment_code_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )

                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("YEAR") },
                        placeholder = { Text("2021") },
                        modifier = Modifier.weight(1f).testTag("equipment_year_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // SAP Code and Power
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = sap,
                        onValueChange = { sap = it },
                        label = { Text("SAP CODE") },
                        placeholder = { Text("REEBH-001") },
                        modifier = Modifier.weight(1f).testTag("equipment_sap_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )

                    OutlinedTextField(
                        value = power,
                        onValueChange = { power = it },
                        label = { Text("POWER / CAPACITY") },
                        placeholder = { Text("100 HP") },
                        modifier = Modifier.weight(1f).testTag("equipment_power_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Project Dropdown / Autocomplete
                ExposedDropdownMenuBox(
                    expanded = projExpanded,
                    onExpandedChange = { projExpanded = !projExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = project,
                        onValueChange = {
                            project = it
                            projExpanded = true
                            val auto = OdConfig.getOdForProject(it)
                            if (auto.isNotBlank()) od = auto
                        },
                        label = { Text("PROJECT / LOCATION") },
                        placeholder = { Text("Select or type site") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("equipment_project_input"),
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
                        expanded = projExpanded,
                        onDismissRequest = { projExpanded = false },
                        modifier = Modifier
                            .background(HacSurfaceElevated)
                            .heightIn(max = 240.dp)
                    ) {
                        filteredProjects.take(20).forEach { p ->
                            val assignedOd = OdConfig.getOdForProject(p)
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(p, color = HacText, fontSize = 13.sp)
                                        if (assignedOd.isNotBlank()) {
                                            Text(assignedOd, color = HacTextMuted, fontSize = 11.sp)
                                        }
                                    }
                                },
                                onClick = {
                                    project = p
                                    od = assignedOd
                                    projExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // O.D Engineer
                OutlinedTextField(
                    value = od,
                    onValueChange = { od = it },
                    label = { Text("O.D ENGINEER") },
                    placeholder = { Text("Auto-filled from project") },
                    modifier = Modifier.fillMaxWidth().testTag("equipment_od_input"),
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

                // Chassis and Engine
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = chassis,
                        onValueChange = { chassis = it },
                        label = { Text("CHASSIS NO.") },
                        modifier = Modifier.weight(1f).testTag("equipment_chassis_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )

                    OutlinedTextField(
                        value = engine,
                        onValueChange = { engine = it },
                        label = { Text("ENGINE MODEL") },
                        modifier = Modifier.weight(1f).testTag("equipment_engine_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HacAccentAmber,
                            unfocusedBorderColor = HacBorder,
                            focusedLabelColor = HacAccentAmber,
                            unfocusedLabelColor = HacTextMuted,
                            focusedTextColor = HacText,
                            unfocusedTextColor = HacText
                        )
                    )
                }

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
                            if (equipmentType.isNotBlank() && code.isNotBlank()) {
                                val item = Equipment(
                                    id = equipment?.id ?: UUID.randomUUID().toString(),
                                    sn = equipment?.sn ?: 0,
                                    category = category,
                                    equipment = equipmentType.trim(),
                                    brand = brand.trim(),
                                    code = code.trim(),
                                    sap = sap.trim(),
                                    chassis = chassis.trim(),
                                    engine = engine.trim(),
                                    year = year.trim(),
                                    power = power.trim(),
                                    project = project.trim(),
                                    od = od.trim(),
                                    costCenter = costCenter.trim(),
                                    value = value.trim(),
                                    createdAt = equipment?.createdAt ?: System.currentTimeMillis()
                                )
                                onSave(item, isNew)
                            }
                        },
                        enabled = equipmentType.isNotBlank() && code.isNotBlank(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HacAccentAmber,
                            contentColor = HacSurface
                        ),
                        modifier = Modifier.testTag("equipment_save_btn")
                    ) {
                        Text(if (isNew) "Save Equipment" else "Update Equipment", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
