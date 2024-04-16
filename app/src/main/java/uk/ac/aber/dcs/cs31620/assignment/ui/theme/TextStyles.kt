package uk.ac.aber.dcs.cs31620.assignment.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

import uk.ac.aber.dcs.cs31620.assignment.R


//Families
val titleFontFamily = FontFamily(
    Font(R.font.black_han_sans_regular,FontWeight.Bold)
)
val contentFontFamily = FontFamily(
    Font(R.font.inter_black,FontWeight.Black),
    Font(R.font.inter_extrabold,FontWeight.ExtraBold),
    Font(R.font.inter_bold,FontWeight.Bold),
    Font(R.font.inter_semibold,FontWeight.SemiBold),
    Font(R.font.inter_regular,FontWeight.Normal),
    Font(R.font.inter_medium,FontWeight.Medium),
    Font(R.font.inter_thin,FontWeight.Thin),
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_extralight,FontWeight.ExtraLight)
)

//Styles
val titleTextStyle: TextStyle = TextStyle(
    color = Color.White,
    fontSize = 35.sp,
    fontFamily = titleFontFamily,
    textAlign = TextAlign.Center
)

val workoutTitleStyle: TextStyle = TextStyle(
    color = Color.White,
    fontSize = 20.sp,
    fontFamily = contentFontFamily,
    fontWeight = FontWeight.ExtraBold
)

val workoutTitleStyleCentered: TextStyle = workoutTitleStyle.plus(TextStyle(
    textAlign = TextAlign.Center
))

val workoutContentStyle: TextStyle = TextStyle(
    color = Color.White,
    fontSize = 15.sp,
    fontFamily = contentFontFamily,
    fontWeight = FontWeight.Medium
)

val workoutContentStyleCentered: TextStyle = workoutContentStyle.plus(TextStyle(
    textAlign = TextAlign.Center
))

val workoutContentStyleAlignedRight: TextStyle = workoutContentStyle.plus(TextStyle(
    textAlign = TextAlign.Right
))

val workoutContentStyleAlignedRightLarge: TextStyle = workoutContentStyleAlignedRight.plus(TextStyle(
    fontSize = 20.sp
))

val workoutContentStyleAlignedLeft: TextStyle = workoutContentStyle.plus(TextStyle(
    textAlign = TextAlign.Left
))

val workoutContentStyleAlignedLeftLarge: TextStyle = workoutContentStyleAlignedLeft.plus(TextStyle(
    fontSize = 20.sp
))

val workoutContentStyleUnderlinedCentered: TextStyle = workoutContentStyleCentered.plus(TextStyle(
    textDecoration = TextDecoration.Underline
))

val runWorkoutTitleStyle: TextStyle = titleTextStyle.plus(TextStyle(
    fontSize = 30.sp
))

val runWorkoutTextStyleBig: TextStyle = titleTextStyle.plus(TextStyle(
    fontSize = 20.sp
))