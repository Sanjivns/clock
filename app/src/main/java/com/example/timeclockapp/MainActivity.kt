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
    val quotes = remember {
        listOf(
            "The best way to predict the future is to create it.",
            "Time is what we want most, but what we use worst.",
            "The bad news is time flies. The good news is you're the pilot.",
            "Yesterday is gone. Tomorrow has not yet come. We have only today. Let us begin.",
            "Lost time is never found again.",
            "Time is money.",
            "Better three hours too soon than a minute too late.",
            "The present moment is filled with joy and happiness. If you are attentive, you will see it.",
            "Time is a created thing. To say 'I don't have time,' is to say 'I don't want to.'",
            "The greatest gift you can give someone is your time because when you give your time, you are giving a portion of your life that you will never get back."
        )
    }
    var currentQuote by remember { mutableStateOf(quotes.random()) }

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

        Text(
            text = "\"${currentQuote}\"",
            fontSize = 14.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { is24Hour = !is24Hour }) {
            Text(if (is24Hour) "Switch to 12-hour" else "Switch to 24-hour")
        }
    }
}

