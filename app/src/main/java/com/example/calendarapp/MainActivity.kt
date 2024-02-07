package com.example.calendarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.example.calendarapp.utils.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarAppTheme {
                Column {
                    var yearInput by remember { mutableStateOf(getString(R.string.year)) }
                    var monthInput by remember { mutableStateOf(getString(R.string.month)) }
                    Text(stringResource(id = R.string.year_input))
                    TextField(
                        value = yearInput,
                        onValueChange = {
                            yearInput = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(stringResource(id = R.string.month_input))
                    TextField(
                        value = monthInput,
                        onValueChange = {
                            monthInput = it
                        },
                        modifier = Modifier.fillMaxWidth()

                    )
                    val year = yearInput.toIntOrNull() ?: 2024 // Use 2024 as default if input is empty or not a valid number
                    val month = monthInput.toIntOrNull() ?: 2 // Use 2 as default if input is empty or not a valid number
                    val monthInfo = MonthInfo(month)
                    Calendar(year, monthInfo)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(year: Int, month: MonthInfo, modifier: Modifier = Modifier.fillMaxSize()) {
    val daysIndex = LocalContext.current.resources.getStringArray(R.array.day_abbreviations)
    val weekOfMonth = getWeeksInMonth(year, month.index)
    val daysOfMonth = getDaysOfMonth(year, month.index)
    var daysSinceFirstJan by remember {
        mutableIntStateOf(0)
    }
    var currentDay by remember {
        mutableIntStateOf(0)
    }
    var workingDays = numberOfWorkDays(year, month.index)

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = month.name,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }, bottomBar = {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {

          
            // Add more icons or content here if needed
        }
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(
            ) {
                Image(
                    painter = painterResource(id = R.drawable.androidparty), // Background image
                    contentDescription = null, // Content description for accessibility
                    contentScale = ContentScale.FillBounds, // Adjust content scale if needed
                    alpha = 0.5F
                )
                Column {
                    Row {
                        Box(modifier = Modifier.padding(20.dp))
                        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier) {
                            items(daysIndex) { dayName ->
                                if (dayName == "S")
                                    Text(text = dayName, fontSize = 35.sp, color = Color.Red)
                                else
                                    Text(text = dayName, fontSize = 35.sp)
                            }
                        }
                    }
                    Row {
                        Column(
                            modifier = Modifier.padding(end = 20.dp)
                        ) {
                            weekOfMonth.forEach {
                                Text(text = "$it", fontSize = 30.sp)
                            }
                        }
                        Column {
                            LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier) {
                                items(daysOfMonth) { day ->
                                    // Display each day of the month as a separate item
                                    // You can customize the UI for each day here

                                    Text(
                                        text = day.toString(),
                                        fontSize = 30.sp,
                                        modifier = Modifier.clickable {
                                            daysSinceFirstJan = numberOfDaysSinceFirstJan(year, month.index, day)
                                            currentDay = if (day != " ") day as Int else 0
                                        }
                                    )

                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(40.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                val workingDaysText = stringResource(R.string.working_days_text, workingDays)
                                val daysSinceFirstJanText = stringResource(
                                    if (daysSinceFirstJan == 0) R.string.click_to_find_days_text
                                    else R.string.days_since_first_jan_text, currentDay, daysSinceFirstJan
                                )

                                Text(
                                    text = workingDaysText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(8.dp)
                                )

                                Text(
                                    text = daysSinceFirstJanText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(8.dp)
                                )

                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalendarAppTheme {
        Calendar(2024, MonthInfo(2))
    }
}



