package chn.phm.pomodoro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import chn.phm.pomodoro.R

val SpaceGroTesk = FontFamily(
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_bold, FontWeight.Bold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    displayMedium = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 80.sp
    ),
    labelLarge = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        fontStyle = FontStyle.Normal
    ),
    titleMedium = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    ),
    labelMedium = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SpaceGroTesk,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
)