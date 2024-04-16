package uk.ac.aber.dcs.cs31620.assignment.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue60
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyle

/**
 * A Standardised button for use across the whole app
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to be applied to this button
 * @param text the text displayed on the button
 * @param content the additional content displayed with this button
 */
@Composable
fun StandardButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: Color = Blue60,
    content: @Composable RowScope.() -> Unit = {},
) {
    Button(
        modifier = modifier
            .width(150.dp)
            .height(40.dp),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        content = {
            Text(
                text = text,
                style = workoutContentStyle,
                textAlign = TextAlign.Center
            )
            content()
        }
    )
}

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconDescription: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = Blue40),
    content: @Composable RowScope.() -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .absolutePadding(
                right = 5.dp,
                top = 5.dp,
                bottom = 5.dp
            )
            .height(60.dp)
            .width(60.dp),
        colors = colors,
        contentPadding = PaddingValues(all = 5.dp),
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = icon,
            contentDescription = iconDescription
        )
        content()
    }
}