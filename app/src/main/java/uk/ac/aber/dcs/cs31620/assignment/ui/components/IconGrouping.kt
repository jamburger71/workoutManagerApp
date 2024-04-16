package uk.ac.aber.dcs.cs31620.assignment.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
/**
 * Wraps two ImageVectors and a label needed for those menu items etc
 * that either use a filled icon or outline icon depending on whether
 * the item is selected or not.
 * @author Chris Loftus
 */
data class IconGrouping(
    val filledIcon: ImageVector,
    val outlineIcon: ImageVector,
    val label: String,
    val iconColor: Color,
    val titleText: String? = null
)