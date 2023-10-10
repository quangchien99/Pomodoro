package chn.phm.pomodoro.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import chn.phm.pomodoro.R
import chn.phm.pomodoro.domain.model.AlarmSound
import chn.phm.pomodoro.domain.model.Background
import chn.phm.pomodoro.domain.model.PomodoroConfig
import chn.phm.pomodoro.ui.helper.CustomDivider
import chn.phm.pomodoro.ui.helper.LargeDropdownMenu
import chn.phm.pomodoro.ui.theme.SelectedColor
import chn.phm.pomodoro.ui.theme.Shapes

@Composable
fun SettingsDialog(
    currentConfig: PomodoroConfig,
    onAlarmSoundSelected: (AlarmSound) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: (PomodoroConfig) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(8.dp),
            shape = Shapes.large
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Header() {
                    onDismissRequest()
                }

                SettingContent(currentConfig, onAlarmSoundSelected) { config ->
                    onConfirmation(config)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Header(onDismissRequest: () -> Unit) {
    IconButton(
        onClick = {
            onDismissRequest()
        },
        modifier = Modifier.align(Alignment.End)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            tint = Color.LightGray,
            contentDescription = "Icon",
            modifier = Modifier.size(16.dp)
        )
    }
    CustomDivider()
}

@Composable
private fun ColumnScope.SettingContent(
    currentConfig: PomodoroConfig,
    onAlarmSoundSelected: (AlarmSound) -> Unit,
    onConfirmation: (PomodoroConfig) -> Unit
) {
    var newInterval by remember { mutableStateOf(currentConfig.longBreakInterval.toString()) }
    var newPomodoroDuration by remember { mutableStateOf(currentConfig.pomodoroDuration.toString()) }
    var newShortBreakDuration by remember { mutableStateOf(currentConfig.shortBreakDuration.toString()) }
    var newLongBreakDuration by remember { mutableStateOf(currentConfig.longBreakDuration.toString()) }
    var selectedThemeIndex by remember {
        mutableStateOf(
            Background.values().indexOf(currentConfig.background)
        )
    }
    var selectedSoundIndex by remember {
        mutableStateOf(
            AlarmSound.values().indexOf(currentConfig.alarmSound)
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .weight(1f, false)
    ) {
        AssistChip(
            onClick = {},
            modifier = Modifier.padding(start = 12.dp),
            label = {
                Text(
                    text = "Timer",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            elevation = null,
            border = null,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        )

        Text(
            text = "Duration (minutes):",
            color = Color.DarkGray,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            durationTextField(newPomodoroDuration, "Pomodoro") {
                newPomodoroDuration = it
            }
            durationTextField(newShortBreakDuration, "Short Break") {
                newShortBreakDuration = it
            }
            durationTextField(newLongBreakDuration, "Long Break") {
                newLongBreakDuration = it
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = "Long Break Interval:",
                style = MaterialTheme.typography.titleMedium
            )

            TextField(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(48.dp)
                    .height(60.dp)
                    .padding(top = 4.dp),
                value = newInterval,
                textStyle = MaterialTheme.typography.labelMedium,
                onValueChange = { newInterval = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        CustomDivider()

        AssistChip(
            onClick = {},
            modifier = Modifier.padding(start = 12.dp),
            label = {
                Text(
                    text = "Sound",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            elevation = null,
            border = null,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sound),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        )

        LargeDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
            items = listOf(
                R.string.alarm_sound_digital_name
            ).map { resourceId ->
                stringResource(id = resourceId)
            },
            selectedIndex = selectedSoundIndex,
            onItemSelected = { index, _ ->
                selectedSoundIndex = index
                onAlarmSoundSelected(AlarmSound.values()[selectedSoundIndex])
            },
        )

        CustomDivider()

        AssistChip(
            onClick = {},
            modifier = Modifier.padding(start = 12.dp),
            label = {
                Text(
                    text = "Theme",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            elevation = null,
            border = null,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_theme),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        )
        LargeDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
            items = listOf(
                R.string.background_galaxy_name,
                R.string.background_night_name,
                R.string.background_rainy_name
            ).map { resourceId ->
                stringResource(id = resourceId)
            },
            selectedIndex = selectedThemeIndex,
            onItemSelected = { index, _ -> selectedThemeIndex = index },
        )

        CustomDivider()

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            OutlinedButton(
                onClick = {
                    onConfirmation(
                        PomodoroConfig(
                            pomodoroDuration = newPomodoroDuration.toInt(),
                            shortBreakDuration = newShortBreakDuration.toInt(),
                            longBreakDuration = newLongBreakDuration.toInt(),
                            longBreakInterval = newInterval.toInt(),
                            background = Background.values()[selectedThemeIndex]
                        )
                    )
                },
                modifier = Modifier
                    .padding(PaddingValues(start = 8.dp))
                    .wrapContentHeight()
                    .width(80.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SelectedColor,
                    contentColor = Color.Gray
                ),
                border = BorderStroke(0.dp, Color.Transparent),
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_save),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun RowScope.durationTextField(
    duration: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .width(100.dp)
            .height(64.dp)
            .align(Alignment.CenterVertically),
        value = duration,
        textStyle = MaterialTheme.typography.titleSmall.copy(
            fontSize = 16.sp
        ),
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = text,
                style = MaterialTheme.typography.labelSmall
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}