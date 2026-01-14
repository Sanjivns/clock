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
                    ClockApp()
                }
            }
        }
    }
}

@Composable
fun ClockApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Clock", "Stopwatch", "Timer")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = { },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> ClockScreen()
                1 -> StopwatchScreen()
                2 -> TimerScreen()
            }
        }
    }
}

@Composable
fun ClockScreen() {
    var is24Hour by remember { mutableStateOf(true) }
    var now by remember { mutableStateOf(Date()) }
    // ... Copy existing quotes logic here if needed, but for brevity keeping it simple or reusing
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

@Composable
fun StopwatchScreen() {
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableLongStateOf(0L) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var displayTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = System.currentTimeMillis()
            while (isRunning) {
                displayTime = elapsedTime + (System.currentTimeMillis() - startTime)
                delay(10) // Update every 10ms
            }
        }
    }

    // Format HH:MM:SS.ms
    val ms = (displayTime % 1000) / 10
    val totalSeconds = displayTime / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    
    val formattedTime = String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, ms)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Stopwatch", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = formattedTime, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                if (!isRunning) {
                    isRunning = true
                }
            }, enabled = !isRunning) {
                Text("Start")
            }
            Button(onClick = {
                if (isRunning) {
                    isRunning = false
                    elapsedTime += System.currentTimeMillis() - startTime
                }
            }, enabled = isRunning) {
                Text("Stop")
            }
            Button(onClick = {
                isRunning = false
                elapsedTime = 0L
                displayTime = 0L
            }) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun TimerScreen() {
    var inputHours by remember { mutableStateOf("") }
    var inputMinutes by remember { mutableStateOf("") }
    var inputSeconds by remember { mutableStateOf("") }
    
    var remainingTime by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning && remainingTime > 0) {
            val endTime = System.currentTimeMillis() + remainingTime * 1000
            while (isRunning && remainingTime > 0) {
                 remainingTime = (endTime - System.currentTimeMillis()) / 1000
                 if (remainingTime <= 0) {
                     remainingTime = 0
                     isRunning = false
                     isFinished = true
                 }
                 delay(100)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Timer", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        if (isRunning || remainingTime > 0 || isFinished) {
            // CountDown Display
             val hours = remainingTime / 3600
             val minutes = (remainingTime % 3600) / 60
             val seconds = remainingTime % 60
             
             Text(
                 text = if(isFinished) "Time's up!" else String.format("%02d:%02d:%02d", hours, minutes, seconds),
                 fontSize = 48.sp, 
                 fontWeight = FontWeight.Bold,
                 color = if (isFinished) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
             )
             
             Spacer(modifier = Modifier.height(32.dp))
             
             Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                 if (!isFinished) {
                     Button(onClick = { isRunning = !isRunning }) {
                         Text(if (isRunning) "Pause" else "Resume")
                     }
                 }
                 Button(onClick = {
                     isRunning = false
                     isFinished = false
                     remainingTime = 0
                 }) {
                     Text("Reset")
                 }
             }
        } else {
            // Inputs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = inputHours, 
                    onValueChange = { if(it.all { c -> c.isDigit() }) inputHours = it },
                    label = { Text("HH") },
                    modifier = Modifier.width(70.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = inputMinutes, 
                    onValueChange = { if(it.all { c -> c.isDigit() }) inputMinutes = it },
                    label = { Text("MM") },
                    modifier = Modifier.width(70.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = inputSeconds, 
                    onValueChange = { if(it.all { c -> c.isDigit() }) inputSeconds = it },
                    label = { Text("SS") },
                     modifier = Modifier.width(70.dp),
                     singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                val h = inputHours.toLongOrNull() ?: 0L
                val m = inputMinutes.toLongOrNull() ?: 0L
                val s = inputSeconds.toLongOrNull() ?: 0L
                remainingTime = h * 3600 + m * 60 + s
                if (remainingTime > 0) {
                    isRunning = true
                    isFinished = false
                }
            }) {
                Text("Start")
            }
        }
    }
}
}

