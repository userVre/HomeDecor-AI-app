package com.ismail.homedecorai.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewQuilt
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.components.WorkspaceImage
import com.ismail.homedecorai.ui.components.ProjectHeaderPreview
import com.ismail.homedecorai.ui.components.ProjectMetricChip
import com.ismail.homedecorai.ui.components.ProjectResultThumb
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.formatProjectDate

@Composable
fun TemporaryUnavailableDialog(
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_temporarily_unavailable_title)) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

@Composable
fun FeedbackDialog(
    busy: Boolean,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var message by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        title = { Text(stringResource(R.string.feedback_dialog_title)) },
        text = {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text(stringResource(R.string.feedback_hint)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 128.dp),
                enabled = !busy,
                minLines = 4,
            )
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(message) },
                enabled = !busy && message.trim().length >= 3,
                shape = CircleShape,
            ) {
                Text(if (busy) stringResource(R.string.feedback_sending) else stringResource(R.string.feedback_send))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !busy, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun DeleteAccountDialog(
    busy: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        title = { Text(stringResource(R.string.delete_account_title)) },
        text = { Text(stringResource(R.string.delete_account_body)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !busy,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            ) {
                Text(if (busy) stringResource(R.string.deleting_account) else stringResource(R.string.delete_account_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !busy, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.logout_title)) },
        text = { Text(stringResource(R.string.logout_body)) },
        confirmButton = {
            Button(onClick = onConfirm, shape = CircleShape) {
                Text(stringResource(R.string.logout_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun LanguagePickerDialog(
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.language_picker_title)) },
        text = {
            Column(
                Modifier
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val systemSelected = currentLanguageTag == AppLocale.SYSTEM_LANGUAGE_TAG
                Surface(
                    color = if (systemSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.language_system_default), fontWeight = FontWeight.SemiBold) },
                        trailingContent = {
                            if (systemSelected) {
                                Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        modifier = Modifier
                            .minimumTouchTarget()
                            .semantics { this.selected = systemSelected }
                            .clickable(role = Role.Button) { onLanguageSelected(AppLocale.SYSTEM_LANGUAGE_TAG) },
                    )
                }
                AppLocale.supportedLanguages.forEach { language ->
                    val selected = language.tag == currentLanguageTag
                    Surface(
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ListItem(
                            headlineContent = { Text(stringResource(language.labelRes), fontWeight = FontWeight.SemiBold) },
                            trailingContent = {
                                if (selected) {
                                    Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            modifier = Modifier
                                .minimumTouchTarget()
                                .semantics { this.selected = selected }
                                .clickable(role = Role.Button) { onLanguageSelected(language.tag) },
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

@Composable
fun FirstLaunchDisclosure(onAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.disclosure_title)) },
        text = {
            Text(stringResource(R.string.disclosure_body))
        },
        confirmButton = {
            Button(onClick = onAccept, shape = CircleShape) {
                Text(stringResource(R.string.understood))
            }
        },
    )
}

@Composable
fun AddToProjectDialog(
    state: HomeDecorUiState,
    result: BoardItem?,
    onDismiss: () -> Unit,
    onCreateProject: () -> Unit,
    onSelectProject: (Project) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.save_to_project), fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    stringResource(R.string.save_to_project_body),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (state.workspace.projects.isEmpty()) {
                    Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            stringResource(R.string.no_projects_yet),
                            modifier = Modifier.padding(14.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.workspace.projects, key = { it.id }) { project ->
                            val alreadyLinked = state.workspace.generatedResults.firstOrNull { it.id == result?.id }?.projectId == project.id
                            Surface(
                                onClick = { onSelectProject(project) },
                                shape = RoundedCornerShape(18.dp),
                                color = if (alreadyLinked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                tonalElevation = 1.dp,
                                border = BorderStroke(1.dp, if (alreadyLinked) MaterialTheme.colorScheme.primary.copy(alpha = 0.36f) else MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    WorkspaceImage(
                                        imageUrl = project.coverImageUrl,
                                        imageUri = project.coverImageUri ?: project.originalPhotoUris.firstOrNull(),
                                        contentDescription = project.name,
                                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(14.dp)),
                                    )
                                    Column(Modifier.weight(1f)) {
                                        Text(project.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(
                                            project.roomType.ifBlank { stringResource(R.string.project_room_unspecified) },
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                    if (alreadyLinked) {
                                        Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onCreateProject, shape = CircleShape) {
                Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.new_project))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun OriginalImageDialog(
    state: HomeDecorUiState,
    result: BoardItem?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.original_image)) },
        text = {
            OriginalSourceImage(
                state = state,
                result = result,
                contentDescription = stringResource(R.string.original_image),
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            )
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

@Composable
fun ProjectDetailDialog(
    project: Project,
    state: HomeDecorUiState,
    onDismiss: () -> Unit,
    onUpdate: (Project) -> Unit,
    onCreateDesign: () -> Unit,
) {
    val projectResults = state.workspace.generatedResults.filter { it.projectId == project.id }
    val projectFavorites = state.workspace.favorites.filter { it.projectId == project.id }
    var name by remember(project.id) { mutableStateOf(project.name) }
    var roomType by remember(project.id) { mutableStateOf(project.roomType) }
    var notes by remember(project.id) { mutableStateOf(project.notes) }
    var styleInfo by remember(project.id) { mutableStateOf(project.styleInfo) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.project_workspace_title), fontWeight = FontWeight.SemiBold) },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    ProjectHeaderPreview(project = project, results = projectResults)
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ProjectMetricChip(Icons.Rounded.PhotoCamera, stringResource(R.string.project_originals_count, project.originalPhotoUris.size + project.originalPhotoUrls.size))
                        ProjectMetricChip(Icons.AutoMirrored.Rounded.ViewQuilt, stringResource(R.string.project_results_count, projectResults.size))
                        ProjectMetricChip(Icons.Rounded.Star, stringResource(R.string.project_favorites_count, projectFavorites.size))
                    }
                }
                item {
                    Text(stringResource(R.string.project_created, formatProjectDate(project.createdAt)), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.project_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        singleLine = true,
                    )
                }
                item {
                    OutlinedTextField(
                        value = roomType,
                        onValueChange = { roomType = it },
                        label = { Text(stringResource(R.string.room_type)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        singleLine = true,
                    )
                }
                item {
                    OutlinedTextField(
                        value = styleInfo,
                        onValueChange = { styleInfo = it },
                        label = { Text(stringResource(R.string.project_style_info)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                    )
                }
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text(stringResource(R.string.project_notes)) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
                        shape = RoundedCornerShape(18.dp),
                        minLines = 3,
                    )
                }
                if (projectResults.isNotEmpty()) {
                    item {
                        Text(stringResource(R.string.project_generated_results), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(projectResults, key = { it.id }) { item ->
                                ProjectResultThumb(title = item.toolTitle, imageUrl = item.imageUrl, imageUri = item.imageUri)
                            }
                        }
                    }
                }
                if (projectFavorites.isNotEmpty()) {
                    item {
                        Text(stringResource(R.string.project_favorites), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(projectFavorites, key = { it.id }) { item ->
                                ProjectResultThumb(title = item.title, imageUrl = item.imageUrl, imageUri = item.imageUri, imageRes = item.imageRes)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(project.copy(name = name.ifBlank { project.name }, roomType = roomType, notes = notes, styleInfo = styleInfo))
                    onDismiss()
                },
                shape = CircleShape,
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onCreateDesign, shape = CircleShape) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.create_design))
            }
        },
    )
}

@Composable
fun ProjectEditorDialog(
    title: String,
    confirmLabel: String,
    initialName: String,
    initialRoomType: String,
    initialNotes: String,
    initialStyleInfo: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    var roomType by remember { mutableStateOf(initialRoomType) }
    var notes by remember { mutableStateOf(initialNotes) }
    var styleInfo by remember { mutableStateOf(initialStyleInfo) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.project_name)) },
                    placeholder = { Text(stringResource(R.string.project_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = roomType,
                    onValueChange = { roomType = it },
                    label = { Text(stringResource(R.string.room_type)) },
                    placeholder = { Text(stringResource(R.string.project_room_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = styleInfo,
                    onValueChange = { styleInfo = it },
                    label = { Text(stringResource(R.string.project_style_info)) },
                    placeholder = { Text(stringResource(R.string.project_style_info_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.project_notes)) },
                    placeholder = { Text(stringResource(R.string.project_notes_hint)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
                    shape = RoundedCornerShape(18.dp),
                    minLines = 3,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name.trim(), roomType.trim(), notes.trim(), styleInfo.trim()) },
                enabled = name.trim().length >= 2,
                shape = CircleShape,
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
fun GenerationErrorNotice(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedButton(
                onClick = onRetry,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(48.dp),
            ) {
                Text(stringResource(R.string.retry), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun OriginalSourceImage(
    state: HomeDecorUiState,
    result: BoardItem?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    WorkspaceImage(
        imageUrl = result?.sourceImageUrl,
        imageUri = result?.sourceImageUri,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
