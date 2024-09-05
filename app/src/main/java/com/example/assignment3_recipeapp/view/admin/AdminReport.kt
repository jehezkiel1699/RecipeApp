package com.example.assignment3_recipeapp.page.admin

import com.example.assignment3_recipeapp.viewModel.UserFirebaseViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.assignment3_recipeapp.model.user.UserFirebase
import com.example.assignment3_recipeapp.ui.theme.Background
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

/**
 * Displays administrative reports using bar and line charts to analyze user demographics and registration trends.
 *
 * @param navController Navigation controller for navigating between composables.
 * @param viewModel ViewModel that supplies user data and statistical analysis.
 */
@Composable
fun AdminReport(navController: NavHostController, viewModel: UserFirebaseViewModel) {
    val users: List<UserFirebase> by viewModel.users.observeAsState(initial = emptyList())
    val years = users.mapNotNull { viewModel.extractYearFromDate(it.registrationDate) }.distinct().sorted()
    var selectedYear by remember { mutableStateOf(years.firstOrNull() ?: "") }
    val maleCount = users.count { it.gender == "Male" }
    val femaleCount = users.count { it.gender == "Female" }

    Box(modifier = Modifier.background(color = Background), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = Background)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Administrator Reports", style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(32.dp))

            Text("Gender Distribution", style = MaterialTheme.typography.titleSmall)
            GenderChartSection(maleCount, femaleCount)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Registration Analysis", style = MaterialTheme.typography.titleSmall)
            YearSelection(years, selectedYear) { year ->
                selectedYear = year
            }
            RegisterChartSection(users.filter { viewModel.extractYearFromDate(it.registrationDate) == selectedYear }, selectedYear)
        }
    }
}
/**
 * Allows selection of a year for detailed analysis.
 *
 * @param years List of years to select from.
 * @param selectedYear The currently selected year.
 * @param onYearSelected Callback invoked when a new year is selected.
 */
@Composable
fun YearSelection(years: List<String>, selectedYear: String, onYearSelected: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .horizontalScroll(scrollState)
        .padding(vertical = 8.dp)) {
        years.forEach { year ->
            Button(
                onClick = { onYearSelected(year) },
                modifier = Modifier.padding(end = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(year, fontSize = 12.sp)
            }
        }
    }
}
/**
 * Composable that renders a bar chart for gender distribution.
 *
 * @param maleCount The count of male users.
 * @param femaleCount The count of female users.
 */
@Composable
fun GenderChartSection(maleCount: Int, femaleCount: Int) {
    val context = LocalContext.current
    val barChart = remember { BarChart(context) }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { barChart }
    ) { bar ->
        setupGenderChart(bar, maleCount, femaleCount)
    }
}
/**
 * Composable that renders a line chart for registration data across months.
 *
 * @param users List of users filtered by the selected year.
 * @param selectedYear Year for which data is displayed.
 */
@Composable
fun RegisterChartSection(users: List<UserFirebase>, selectedYear: String) {
    val context = LocalContext.current
    val lineChart = remember { LineChart(context) }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { lineChart }
    ) { line ->
        setupRegisterChart(line, selectedYear, users)
    }
}
/**
 * Sets up a gender distribution bar chart.
 *
 * @param barChart The bar chart to configure.
 * @param maleCount The count of male users.
 * @param femaleCount The count of female users.
 */
fun setupGenderChart(barChart: BarChart, maleCount: Int, femaleCount: Int) {
    val entries = listOf(
        BarEntry(0f, maleCount.toFloat()),
        BarEntry(1f, femaleCount.toFloat())
    )
    val dataSet = BarDataSet(entries, "Gender Distribution")
    dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

    barChart.apply {
        data = BarData(dataSet)
        description.text = "Gender Distribution"
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Male", "Female"))
        axisLeft.axisMinimum = 0f
        animateY(500)
    }
}
/**
 * Sets up a line chart to display monthly registration data for a selected year.
 *
 * @param lineChart The line chart to configure.
 * @param selectedYear Year for which data is displayed.
 * @param users List of users filtered by the selected year.
 */
fun setupRegisterChart(lineChart: LineChart, selectedYear: String, users: List<UserFirebase>) {
    val monthEntries = users.filter { it.registrationDate?.endsWith(selectedYear) == true }
        .groupBy { it.registrationDate!!.substring(3, 5) }
        .map { (month, list) ->
            Entry(month.toFloat() - 1, list.size.toFloat())
        }
        .sortedBy { it.x }

    val dataSet = LineDataSet(monthEntries, "Registrations Over Time")
    dataSet.color = Color.Black.toArgb()  // Convert Compose color to Android integer color
    dataSet.lineWidth = 2.5f    // Set the line width
    dataSet.setCircleColor(Color.Black.toArgb())  // Set data point color using Compose color converted to Android color
    dataSet.setCircleRadius(5f) // Set data point size

    lineChart.apply {
        data = LineData(dataSet)
        description.text = "Monthly Registrations for $selectedYear"
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"))
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        axisLeft.axisMinimum = 0f
        animateY(500)
    }
}

