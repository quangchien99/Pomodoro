package chn.phm.pomodoro.ui.screen

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import chn.phm.pomodoro.system.service.PomodoroService
import chn.phm.pomodoro.ui.PomodoroAction
import chn.phm.pomodoro.ui.PomodoroViewModel
import chn.phm.pomodoro.ui.dialog.SettingsDialog
import chn.phm.pomodoro.ui.helper.PomodoroIconButton
import chn.phm.pomodoro.ui.helper.PomodoroOutlinedButton
import chn.phm.pomodoro.ui.theme.PomodoroColor
import chn.phm.pomodoro.ui.theme.Shapes
import chn.phm.pomodoro.utils.PomodoroHelper.convertToMinuteFormat
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import java.util.Locale

@Composable
fun PomodoroScreen(
    context: Context,
    pomodoroViewModel: PomodoroViewModel
) {
    val currentPomodoro by pomodoroViewModel.currentPomodoro
    val currentPomodoroConfig by pomodoroViewModel.currentPomodoroConfig
    val isOpenSettingDialog = remember { mutableStateOf(false) }

    ObservePomodoroActions(pomodoroViewModel)

    if (isOpenSettingDialog.value) {
        SettingsDialog(
            currentConfig = currentPomodoroConfig,
            onAlarmSoundSelected = { selectedAlarmSound ->
                pomodoroViewModel.selectedSound(selectedAlarmSound.resId)
            },
            onDismissRequest = {
                isOpenSettingDialog.value = false
            }
        ) {
            isOpenSettingDialog.value = false
            pomodoroViewModel.updateConfig(it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(
                        currentPomodoroConfig.background.resId
                    )
                    .build(),
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = "Back ground image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (currentPomodoro.state != PomodoroState.COUNTING) {
            IconButton(
                onClick = { isOpenSettingDialog.value = true },
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
            PomodoroModes(pomodoroViewModel, currentPomodoro)

            Text(
                text = currentPomodoro.remainingTime.convertToMinuteFormat(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            PomodoroActions(context, pomodoroViewModel, currentPomodoro)
        }
    }
}

@Composable
private fun PomodoroModes(
    pomodoroViewModel: PomodoroViewModel,
    currentPomodoro: Pomodoro
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        PomodoroOutlinedButton(
            currentPomodoro.timerType == TimerType.POMODORO,
            pomodoroViewModel,
            TimerType.POMODORO
        ) {
            Text(
                text = stringResource(id = R.string.title_pomodoro),
                style = if (currentPomodoro.timerType == TimerType.POMODORO) {
                    MaterialTheme.typography.displayLarge
                } else {
                    MaterialTheme.typography.displayMedium
                }
            )
        }

        PomodoroOutlinedButton(
            currentPomodoro.timerType == TimerType.SHORT_BREAK,
            pomodoroViewModel,
            TimerType.SHORT_BREAK
        ) {
            Text(
                text = stringResource(id = R.string.title_short_break),
                style = if (currentPomodoro.timerType == TimerType.SHORT_BREAK) {
                    MaterialTheme.typography.displayLarge
                } else {
                    MaterialTheme.typography.displayMedium
                }
            )
        }

        PomodoroOutlinedButton(
            currentPomodoro.timerType == TimerType.LONG_BREAK,
            pomodoroViewModel,
            TimerType.LONG_BREAK
        ) {
            Text(
                text = stringResource(id = R.string.title_long_break),
                style = if (currentPomodoro.timerType == TimerType.LONG_BREAK) {
                    MaterialTheme.typography.displayLarge
                } else {
                    MaterialTheme.typography.displayMedium
                }
            )
        }
    }
}

@Composable
private fun PomodoroActions(
    context: Context,
    pomodoroViewModel: PomodoroViewModel,
    currentPomodoro: Pomodoro
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        if (currentPomodoro.state == PomodoroState.PAUSED) {
            PomodoroIconButton(
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
                containerColor = Color.White, // Set the background color to white
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
                style = MaterialTheme.typography.labelLarge
            )
        }
        if (currentPomodoro.state == PomodoroState.PAUSED) {
            PomodoroIconButton(
                onClick = { pomodoroViewModel.next() },
                iconResourceId = R.drawable.ic_next
            )
        }
    }
}

@Composable
private fun ObservePomodoroActions(viewModel: PomodoroViewModel) {
    val actionEvent by viewModel.pomodoroActionEvent
    val context = LocalContext.current

    actionEvent.getContentIfNotHandled()?.let { action ->
        Log.e("Pomodoro","Event= $action")
        when (action) {
            is PomodoroAction.PlaySound -> {
                val mediaPlayer = MediaPlayer.create(context, action.soundId)
                mediaPlayer?.start()
                mediaPlayer?.setOnCompletionListener { it.release() }
            }
            is PomodoroAction.StartCountingService -> {
                val startIntent = Intent(context, PomodoroService::class.java).apply {
                    putExtra("remainingTime", action.countingTime)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(startIntent)
                } else {
                    context.startService(startIntent)
                }
            }
            PomodoroAction.StopCountingService -> {
                val stopIntent = Intent(context, PomodoroService::class.java)
                context.stopService(stopIntent)
            }
            else -> {
                //do nothing
            }
        }
    }
}