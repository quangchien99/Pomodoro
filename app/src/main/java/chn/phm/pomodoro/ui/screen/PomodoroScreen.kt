package chn.phm.pomodoro.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import chn.phm.pomodoro.R
import chn.phm.pomodoro.domain.model.Pomodoro
import chn.phm.pomodoro.domain.model.PomodoroState
import chn.phm.pomodoro.domain.model.TimerType
import chn.phm.pomodoro.ui.PomodoroViewModel
import chn.phm.pomodoro.ui.theme.PomodoroColor
import chn.phm.pomodoro.ui.theme.SelectedColor
import chn.phm.pomodoro.ui.theme.Shapes
import chn.phm.pomodoro.utils.PomodoroHelper.convertToMinuteFormat
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import java.util.Locale

@Composable
fun PomodoroScreen(
    pomodoroViewModel: PomodoroViewModel
) {
    val currentPomodoro by pomodoroViewModel.currentPomodoro

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.bg_galaxy)
                    .build(),
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = "Back ground image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (currentPomodoro.state != PomodoroState.COUNTING) {
            IconButton(
                onClick = { /* Handle setting icon click */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    tint = Color.White,
                    contentDescription = "Icon setting",
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            pomodoroModes(pomodoroViewModel, currentPomodoro)

            Text(
                text = currentPomodoro.remainingTime.convertToMinuteFormat(),
                style = MaterialTheme.typography.caption,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            pomodoroActions(pomodoroViewModel, currentPomodoro)
        }
    }
}

@Composable
private fun pomodoroModes(
    pomodoroViewModel: PomodoroViewModel,
    currentPomodoro: Pomodoro
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        createOutlinedButton(
            currentPomodoro.timerType == TimerType.POMODORO,
            pomodoroViewModel,
            TimerType.POMODORO
        ) {
            Text(
                text = stringResource(id = R.string.title_pomodoro),
                style = if (currentPomodoro.timerType == TimerType.POMODORO) {
                    MaterialTheme.typography.h1
                } else {
                    MaterialTheme.typography.h2
                }
            )
        }

        createOutlinedButton(
            currentPomodoro.timerType == TimerType.SHORT_BREAK,
            pomodoroViewModel,
            TimerType.SHORT_BREAK
        ) {
            Text(
                text = stringResource(id = R.string.title_short_break),
                style = if (currentPomodoro.timerType == TimerType.SHORT_BREAK) {
                    MaterialTheme.typography.h1
                } else {
                    MaterialTheme.typography.h2
                }
            )
        }

        createOutlinedButton(
            currentPomodoro.timerType == TimerType.LONG_BREAK,
            pomodoroViewModel,
            TimerType.LONG_BREAK
        ) {
            Text(
                text = stringResource(id = R.string.title_long_break),
                style = if (currentPomodoro.timerType == TimerType.LONG_BREAK) {
                    MaterialTheme.typography.h1
                } else {
                    MaterialTheme.typography.h2
                }
            )
        }
    }
}

@Composable
private fun pomodoroActions(
    pomodoroViewModel: PomodoroViewModel,
    currentPomodoro: Pomodoro
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        if (currentPomodoro.state == PomodoroState.PAUSED) {
            createIconButton(
                onClick = { pomodoroViewModel.restart() },
                iconResourceId = R.drawable.ic_restart
            )
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = {
                when (currentPomodoro.state) {
                    PomodoroState.READY -> {
                        pomodoroViewModel.start()
                    }
                    PomodoroState.COUNTING -> {
                        pomodoroViewModel.pause()
                    }
                    PomodoroState.PAUSED -> {
                        pomodoroViewModel.resume()
                    }
                    PomodoroState.FINISHED -> {
                        //do nothing
                    }
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.White, // Set the background color to white
                contentColor = PomodoroColor // Set the text color
            ),
            shape = Shapes.large
        ) {
            Text(
                text = when (currentPomodoro.state) {
                    PomodoroState.READY -> {
                        stringResource(id = R.string.title_start).uppercase(Locale.ROOT)
                    }
                    PomodoroState.COUNTING -> {
                        stringResource(id = R.string.title_pause).uppercase(Locale.ROOT)
                    }
                    PomodoroState.PAUSED -> {
                        stringResource(id = R.string.title_resume).uppercase(Locale.ROOT)
                    }
                    PomodoroState.FINISHED -> {
                        stringResource(id = R.string.title_start).uppercase(Locale.ROOT)
                    }
                },
                style = MaterialTheme.typography.button
            )
        }
        if (currentPomodoro.state == PomodoroState.PAUSED) {
            createIconButton(
                onClick = { pomodoroViewModel.next() },
                iconResourceId = R.drawable.ic_next
            )
        }
    }
}


@Composable
private fun createIconButton(
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
private fun createOutlinedButton(
    isSelected: Boolean,
    pomodoroViewModel: PomodoroViewModel,
    timerType: TimerType,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = { pomodoroViewModel.changeType(timerType) },
        modifier = Modifier.padding(PaddingValues(start = 8.dp)),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (isSelected) SelectedColor else Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(0.dp, Color.Transparent)
    ) {
        content()
    }
}