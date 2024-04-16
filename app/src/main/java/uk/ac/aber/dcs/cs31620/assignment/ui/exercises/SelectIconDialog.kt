package uk.ac.aber.dcs.cs31620.assignment.ui.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.ac.aber.dcs.cs31620.assignment.R
import uk.ac.aber.dcs.cs31620.assignment.ui.components.StandardButton
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.Blue40
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleCentered
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutContentStyleUnderlinedCentered
import uk.ac.aber.dcs.cs31620.assignment.ui.theme.workoutTitleStyle

val iconRefArray: IntArray = intArrayOf(
    R.drawable.benchpress,
    R.drawable.bigball,
    R.drawable.cycle,
    R.drawable.cyclingmachine,
    R.drawable.deadlift,
    R.drawable.hoop,
    R.drawable.kickbag,
    R.drawable.pullups,
    R.drawable.punchbag,
    R.drawable.run,
    R.drawable.runningmachine,
    R.drawable.selflift,
    R.drawable.skip,
    R.drawable.smallweights
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectIconDialog(
    visible: MutableState<Boolean>,
    iconVariable: MutableState<Int>,
    currentColor: MutableState<Color>
) {
    val originalIcon = remember{ iconVariable.value }
    val originalColor = remember{ currentColor.value }
    if (visible.value) {
        Dialog(
            onDismissRequest = {
                iconVariable.value = originalIcon
                currentColor.value = originalColor
                visible.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .background(color = Blue40)
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.addExercise_title_selectIcon),
                    style = workoutTitleStyle
                )
                Divider(color = Color.White)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .verticalScroll(state = rememberScrollState(0))
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    iconRefArray.forEach { ref ->
                        Row {
                            Button(modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .wrapContentHeight(),
                                onClick = {
                                    iconVariable.value = ref
                                },
                                colors = (if (iconVariable.value == ref)
                                    ButtonDefaults.buttonColors()
                                        else
                                    ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                        )
                            ) {
                                Icon(modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .wrapContentHeight(),
                                    painter = painterResource(id = ref),
                                    contentDescription = stringResource(id = R.string.exercise_icon_default_description),
                                    tint = currentColor.value
                                )
                            }
                        }
                    }
                }
                Divider(color = Color.White)
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier
                            .weight(4f)
                            .height(50.dp),
                        value = currentColor.value.red*255,
                        onValueChange = {
                            currentColor.value = Color(
                                red = it/255,
                                green = currentColor.value.green,
                                blue = currentColor.value.blue,
                                alpha = currentColor.value.alpha
                            )
                        },
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.Red,
                            inactiveTrackColor = Color.Red,
                            thumbColor = Color.White
                        ),
                        steps = 256,
                        valueRange = 0f..255f
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = (currentColor.value.red * 255).toInt().toString(),
                        style = workoutContentStyleCentered
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier
                            .weight(4f)
                            .height(50.dp),
                        value = currentColor.value.green*255,
                        onValueChange = {
                            currentColor.value = Color(
                                red = currentColor.value.red,
                                green = it/255,
                                blue = currentColor.value.blue,
                                alpha = currentColor.value.alpha
                            )
                        },
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.Green,
                            inactiveTrackColor = Color.Green,
                            thumbColor = Color.White
                        ),
                        steps = 256,
                        valueRange = 0f..255f
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = (currentColor.value.green * 255).toInt().toString(),
                        style = workoutContentStyleCentered
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier
                            .weight(4f)
                            .height(50.dp),
                        value = currentColor.value.blue*255,
                        onValueChange = {
                            currentColor.value = Color(
                                red = currentColor.value.red,
                                green = currentColor.value.green,
                                blue = it/255,
                                alpha = currentColor.value.alpha
                            )
                        },
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.Blue,
                            inactiveTrackColor = Color.Blue,
                            thumbColor = Color.White
                        ),
                        steps = 256,
                        valueRange = 0f..255f
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = (currentColor.value.blue * 255).toInt().toString(),
                        style = workoutContentStyleCentered
                    )
                }
                Divider(color = Color.White)
                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 10.dp)
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StandardButton(
                        modifier = Modifier,
                        text = stringResource(id = R.string.button_cancel),
                        onClick = {
                            iconVariable.value = originalIcon
                            currentColor.value = originalColor
                            visible.value = false
                        }
                    )
                    StandardButton(
                        modifier = Modifier,
                        text = stringResource(id = R.string.button_confirm),
                        onClick = { visible.value = false }
                    )
                }
                val uriHandler = LocalUriHandler.current
                val uriLink = stringResource(id = R.string.icon_link)
                val uriText = AnnotatedString(stringResource(id = R.string.icon_disclaimer))
                ClickableText(
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .wrapContentHeight()
                        .height(50.dp),
                    text = uriText,
                    style = workoutContentStyleUnderlinedCentered,
                    onClick = {
                        uriHandler.openUri(uriLink)
                    }
                )
            }
        }
    }
}