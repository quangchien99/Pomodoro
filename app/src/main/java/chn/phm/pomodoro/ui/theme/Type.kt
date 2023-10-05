package chn.phm.pomodoro.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import chn.phm.pomodoro.R

val SpaceGroTesk = FontFamily(
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_bold, FontWeight.Bold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    h2 = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    caption = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 80.sp
    ),
    button = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        fontStyle = FontStyle.Normal
    ),
)