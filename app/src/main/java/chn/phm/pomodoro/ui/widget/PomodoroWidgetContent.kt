package chn.phm.pomodoro.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontFamily
import androidx.glance.text.FontStyle
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import chn.phm.pomodoro.PomodoroActivity
import chn.phm.pomodoro.R
import chn.phm.pomodoro.data.datastore.serializer.PomodoroConfigSerializer.pomodoroConfigDataStore
import chn.phm.pomodoro.domain.model.Pomodoro
import chn.phm.pomodoro.domain.model.PomodoroConfig
import chn.phm.pomodoro.domain.model.PomodoroState
import chn.phm.pomodoro.domain.model.TimerType

@Composable
fun PomodoroWidgetContent() {
    val context = LocalContext.current
    val config = context.pomodoroConfigDataStore.data.collectAsState(initial = null).value
    if (config == null) {
        LoadingContent()
    } else {
        PomodoroContent(context, config)
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = R.color.white),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading...",
            modifier = GlanceModifier.padding(16.dp)
        )
    }
}

@Composable
fun PomodoroContent(context: Context, config: PomodoroConfig) {
    val openAppIntent = actionStartActivity<PomodoroActivity>()

    val currentPomodoro = mutableStateOf(Pomodoro())
    currentPomodoro.value.timerType = TimerType.POMODORO
    currentPomodoro.value.remainingTime = config.pomodoroDuration
    currentPomodoro.value.state = PomodoroState.READY
    Box(
        modifier = GlanceModifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            provider = ImageProvider(resId = config.background.resId),
            contentDescription = "Background image",
            modifier = GlanceModifier.fillMaxSize().clickable(onClick = openAppIntent),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "Pomodoro",
                    onClick = { /*TODO*/ },
                    modifier = GlanceModifier.defaultWeight()
                )
                Spacer(modifier = GlanceModifier.size(8.dp))
                Button(
                    text = "Short Break",
                    onClick = { /*TODO*/ },
                    modifier = GlanceModifier.defaultWeight()
                )
                Spacer(modifier = GlanceModifier.size(8.dp))
                Button(
                    text = "Long Break",
                    onClick = { /*TODO*/ },
                    modifier = GlanceModifier.defaultWeight()

                )
            }
            Text(
                text = "25:00",
                maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(R.color.white),
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 60.sp
                )
            )

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    provider = ImageProvider(resId = R.drawable.ic_restart),
                    contentDescription = "",
                    modifier = GlanceModifier.size(32.dp)
                        .clickable(onClick = openAppIntent),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = GlanceModifier.size(8.dp))
                Button(
                    text = "Start",
                    onClick = { /*TODO*/ },
                    modifier = GlanceModifier.wrapContentHeight()
                        .width(160.dp)
                )
                Spacer(modifier = GlanceModifier.size(8.dp))
                Image(
                    provider = ImageProvider(resId = R.drawable.ic_next),
                    contentDescription = "",
                    modifier = GlanceModifier.size(32.dp)
                        .clickable(onClick = openAppIntent),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
