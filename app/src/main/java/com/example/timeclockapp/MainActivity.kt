package com.example.timeclockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timeclockapp.ui.theme.TimeClockAppTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeClockAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimeScreen()
                }
            }
        }
    }
}

@Composable
fun TimeScreen() {
    var is24Hour by remember { mutableStateOf(true) }
    var now by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = Date()
            delay(1000)
        }
    }

    val timeFormat = remember(is24Hour) {
        if (is24Hour) SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        else SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    }
    val dateFormat = remember {
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    }

    val timeText = timeFormat.format(now)
    val dateText = dateFormat.format(now)
    val timeZone = TimeZone.getDefault()
    val tzLabel =
        "Local time zone: ${timeZone.getDisplayName(timeZone.inDaylightTime(now), TimeZone.SHORT)}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Current Time",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.15.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = timeText,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = dateText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = tzLabel,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { is24Hour = !is24Hour }) {
            Text(if (is24Hour) "Switch to 12-hour" else "Switch to 24-hour")
        }
    }
}

