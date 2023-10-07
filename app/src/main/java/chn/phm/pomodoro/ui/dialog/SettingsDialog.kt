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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import chn.phm.pomodoro.R
import chn.phm.pomodoro.ui.theme.SelectedColor
import chn.phm.pomodoro.ui.theme.Shapes

@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
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

                SettingContent() {
                    onConfirmation()
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
private fun ColumnScope.SettingContent(onConfirmation: () -> Unit) {
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
            durationTextField(25, "Pomodoro")
            durationTextField(5, "Short Break")
            durationTextField(15, "Long Break")
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

            var newInterval by remember { mutableStateOf("4") }

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
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 16.dp),
                text = "Alarm Sound:",
                style = MaterialTheme.typography.titleMedium
            )
        }

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
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 16.dp),
                text = "Background:",
                style = MaterialTheme.typography.titleMedium
            )
        }

        CustomDivider()

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            OutlinedButton(
                onClick = { onConfirmation() },
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
private fun RowScope.durationTextField(duration: Int, text: String) {
    var newDuration by remember { mutableStateOf("$duration") }

    TextField(
        modifier = Modifier
            .width(100.dp)
            .height(48.dp)
            .align(Alignment.CenterVertically),
        value = newDuration,
        textStyle = MaterialTheme.typography.titleSmall,
        onValueChange = { newDuration = it },
        label = {
            Text(
                text, style = MaterialTheme.typography.labelSmall
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

@Composable
private fun CustomDivider() {
    Divider(
        thickness = 1.dp,
        color = Color.LightGray,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    )
}
