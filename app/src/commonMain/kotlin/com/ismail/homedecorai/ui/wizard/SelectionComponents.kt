package com.ismail.homedecorai.ui.wizard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.disabledSemantics

// ---------------------------------------------------------------------------
// SelectionCard
// ---------------------------------------------------------------------------

/**
 * Unified selection card supporting single-select and multi-select modes.
 *
 * Consistent selected state across all wizard screens:
 * - 2 dp teal border
 * - Pale teal selected container (`primaryContainer` at 18% alpha)
 * - Checkmark badge in the top-right corner
 * - Bold selected label
 * - Accessible selected semantics
 *
 * @param label the option label (never truncated)
 * @param isSelected whether this card is currently selected
 * @param onClick called when the card is clicked
 * @param modifier modifier applied to the card root
 * @param enabled whether the card is interactive (default true)
 * @param description optional supporting text below the label
 * @param icon optional leading icon composable (e.g. room type icon)
 * @param thumbnail optional thumbnail content (e.g. for image cards)
 * @param selectedBadge optional custom badge when selected (defaults to checkmark circle)
 */
@Composable
fun SelectionCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    icon: @Composable (() -> Unit)? = null,
    thumbnail: @Composable (() -> Unit)? = null,
    selectedBadge: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "selectionCardScale",
    )

    val borderColor = when {
        !enabled -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        isSelected -> HomeDecorExtra.surfaceBorderSelected
        isHovered -> HomeDecorExtra.surfaceBorderHover
        else -> HomeDecorExtra.surfaceBorder.copy(alpha = 0.5f)
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp

    val bgColor = when {
        !enabled -> MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.3f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)
    }

    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = bgColor,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.wizardOptionCard, label))
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .disabledSemantics(enabled)
            .scale(scale)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = HomeDecorShape.Card,
            ),
    ) {
        Box {
            // Thumbnail or icon-based content
            if (thumbnail != null) {
                thumbnail()
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (icon != null) {
                        // Icon circle
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier.size(32.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                icon()
                            }
                        }
                        Spacer(Modifier.width(10.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            label,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            maxLines = 2,
                        )
                        if (description != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                            )
                        }
                    }
                }
            }

            // Checkmark badge (top-right)
            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn(spring(stiffness = Spring.StiffnessHigh)) + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp),
            ) {
                if (selectedBadge != null) {
                    selectedBadge()
                } else {
                    DefaultCheckBadge()
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// DefaultCheckBadge
// ---------------------------------------------------------------------------

@Composable
fun DefaultCheckBadge() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// SelectionGrid
// ---------------------------------------------------------------------------

/**
 * Responsive selection grid that arranges items in rows.
 *
 * Default responsive columns:
 * - Desktop (>=1024 dp): 4 columns
 * - Tablet (768-1023 dp): 3 columns
 * - Mobile (<768 dp): 2 columns
 *
 * Override with [columns] to force a specific column count.
 *
 * @param columns fixed column count, or null for responsive auto
 * @param modifier modifier applied to the grid container
 * @param horizontalSpacing horizontal spacing between cards (dp)
 * @param verticalSpacing vertical spacing between cards (dp)
 * @param items list of composable lambdas, one per card
 */
@Composable
fun SelectionGrid(
    modifier: Modifier = Modifier,
    columns: Int? = null,
    horizontalSpacing: Int = 12,
    verticalSpacing: Int = 12,
    items: List<@Composable () -> Unit>,
) {
    val isDesktop = rememberIsDesktop()

    val effectiveColumns = columns ?: when {
        isDesktop -> 4
        else -> 2
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing.dp),
    ) {
        items.chunked(effectiveColumns).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing.dp),
            ) {
                row.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        item()
                    }
                }
                // Fill remaining space if row is incomplete
                repeat(effectiveColumns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// SelectionGroup
// ---------------------------------------------------------------------------

/**
 * Section wrapper with heading, optional description, requirement text, and
 * selection count. Wraps a grid of [SelectionCard]s.
 *
 * @param title section heading (e.g. "Room Type", "Design Style")
 * @param description optional supporting text below the heading
 * @param requirementText optional requirement hint (e.g. "Select a room type to continue")
 * @param selectedCount current number of selected items (null to hide count)
 * @param maxSelections maximum selectable items (for multi-select)
 * @param modifier modifier applied to the group container
 * @param content the grid content
 */
@Composable
fun SelectionGroup(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    requirementText: String? = null,
    selectedCount: Int? = null,
    maxSelections: Int? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Section header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() },
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            if (description != null) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Requirement message
        if (requirementText != null) {
            SelectionRequirementMessage(text = requirementText)
        }

        // Selection count
        if (selectedCount != null && maxSelections != null) {
            SelectionSummary(
                selectedCount = selectedCount,
                maxSelections = maxSelections,
            )
        }

        // Content (grid)
        content()
    }
}

// ---------------------------------------------------------------------------
// SelectionSummary
// ---------------------------------------------------------------------------

/**
 * Displays selection count, e.g. "2 of 3 goals selected".
 *
 * @param selectedCount number of currently selected items
 * @param maxSelections maximum number of selectable items
 * @param modifier modifier applied to the summary container
 * @param label optional custom label (defaults to "selected")
 */
@Composable
fun SelectionSummary(
    selectedCount: Int,
    maxSelections: Int,
    modifier: Modifier = Modifier,
    label: String = "selected",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Count indicator dots
        repeat(maxSelections) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index < selectedCount)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
            )
            if (index < maxSelections - 1) {
                Spacer(Modifier.width(4.dp))
            }
        }

        Spacer(Modifier.width(8.dp))

        Text(
            "$selectedCount of $maxSelections $label",
            style = MaterialTheme.typography.labelMedium,
            color = if (selectedCount > 0)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selectedCount > 0) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

// ---------------------------------------------------------------------------
// SelectionRequirementMessage
// ---------------------------------------------------------------------------

/**
 * Inline requirement text beside the section heading.
 * Shown when a required selection hasn't been made yet.
 *
 * @param text the requirement message (e.g. "Select a room type to continue")
 * @param modifier modifier applied to the message container
 */
@Composable
fun SelectionRequirementMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Teal dot indicator
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Helper: compute responsive column count
// ---------------------------------------------------------------------------

/**
 * Returns the responsive column count based on screen width.
 * - Desktop (>=1024 dp): 4 columns
 * - Tablet (768-1023 dp): 3 columns
 * - Mobile (<768 dp): 2 columns
 */
fun responsiveColumns(): Int {
    val screenWidth = com.ismail.homedecorai.getScreenWidthDp()
    return when {
        screenWidth >= 1024 -> 4
        screenWidth >= 768 -> 3
        else -> 2
    }
}

/**
 * Returns the responsive column count with an optional override.
 * If [override] is non-null, it is used directly.
 */
fun responsiveColumns(override: Int?): Int = override ?: responsiveColumns()
