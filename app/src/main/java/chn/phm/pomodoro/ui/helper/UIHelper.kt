package chn.phm.pomodoro.ui.helper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import chn.phm.pomodoro.domain.model.TimerType
import chn.phm.pomodoro.ui.PomodoroViewModel
import chn.phm.pomodoro.ui.theme.SelectedColor

object UIHelper {

    @Composable
    fun createIconButton(
        onClick: () -> Unit,
        iconResourceId: Int,
        modifier: Modifier = Modifier
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier.padding(end = 16.dp, start = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResourceId),
                tint = Color.White,
                contentDescription = "Icon",
                modifier = Modifier.size(36.dp)
            )
        }
    }

    @Composable
    fun createOutlinedButton(
        isSelected: Boolean,
        pomodoroViewModel: PomodoroViewModel,
        timerType: TimerType,
        content: @Composable () -> Unit
    ) {
        OutlinedButton(
            onClick = { pomodoroViewModel.changeType(timerType) },
            modifier = Modifier.padding(PaddingValues(start = 8.dp)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isSelected) SelectedColor else Color.Transparent,
                contentColor = Color.White
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            contentPadding = PaddingValues(8.dp)
        ) {
            content()
        }
    }
}