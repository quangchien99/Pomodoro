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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chn.phm.pomodoro.R
import chn.phm.pomodoro.ui.theme.PomodoroColor
import chn.phm.pomodoro.ui.theme.SelectedColor
import chn.phm.pomodoro.ui.theme.Shapes
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import java.util.*

@Composable
fun PomodoroScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.bg_galaxy)
                    .build(),
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = { /* Handle setting icon click */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(PaddingValues(start = 8.dp)),
                    onClick = {},
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = SelectedColor,
                        contentColor = Color.White // Set the text color
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.title_pomodoro),
                        style = MaterialTheme.typography.h1
                    )
                }
                OutlinedButton(
                    modifier = Modifier.padding(PaddingValues(start = 8.dp)),
                    onClick = {},
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.White // Set the text color
                    ),
                    border = BorderStroke(0.dp, Color.Transparent)
                ) {
                    Text(
                        text = stringResource(id = R.string.title_short_break),
                        style = MaterialTheme.typography.h2,
                    )
                }
                OutlinedButton(
                    modifier = Modifier.padding(PaddingValues(start = 8.dp, end = 8.dp)),
                    onClick = {},
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.White // Set the text color
                    ),
                    border = BorderStroke(0.dp, Color.Transparent)
                ) {
                    Text(
                        text = stringResource(id = R.string.title_long_break),
                        style = MaterialTheme.typography.h2,
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.content_timer_pomodoro),
                style = MaterialTheme.typography.caption,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                IconButton(
                    onClick = { /* Handle setting icon click */ },
                    modifier = Modifier
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_restart),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.White, // Set the background color to white
                        contentColor = PomodoroColor // Set the text color
                    ),
                    shape = Shapes.large
                ) {
                    Text(
                        text = stringResource(id = R.string.title_start).uppercase(Locale.ROOT),
                        style = MaterialTheme.typography.button
                    )
                }

                IconButton(
                    onClick = { /* Handle setting icon click */ },
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_next),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun PomodoroScreenPreview() {
    PomodoroScreen()
}