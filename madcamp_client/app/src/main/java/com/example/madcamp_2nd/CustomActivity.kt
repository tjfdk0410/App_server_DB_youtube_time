package com.example.madcamp_2nd

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CustomActivity : AppCompatActivity() {

    lateinit var tvUsageStats: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_activity)
        tvUsageStats = findViewById(R.id.UsageState)
        showUsageStats()
    }

    private fun showUsageStats() {
        var usageStatsManager: UsageStatsManager =
            getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        var queryUsageStats: List<UsageStats> = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis, System.currentTimeMillis())
        var stats_data: String = ""
        for (i in 0..queryUsageStats.size - 1) {
            stats_data =
                stats_data + "Package Name : " + queryUsageStats.get(i).packageName + "\n" +
                        "Last Time Used : " + convertTime1(queryUsageStats.get(i).lastTimeUsed) + "\n" +
                        "Describe Contents : " + queryUsageStats.get(i).describeContents() + "\n" +
                        "First Time Stamp : " + convertTime1(queryUsageStats.get(i).firstTimeStamp) + "\n" +
                        "Last Time Stamp : " + convertTime1(queryUsageStats.get(i).lastTimeStamp) + "\n" +
                        "Total Time in Foreground : " + convertTime2(queryUsageStats.get(i).totalTimeInForeground) + "\n\n"
        }
        tvUsageStats.setText(stats_data)
    }

    private fun convertTime1(lastTimeUsed: Long): String {
        var date: Date = Date(lastTimeUsed)
        var format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        return format.format(date)
    }


    private fun convertTime2(lastTimeUsed: Long): String {
        var date: Date = Date(lastTimeUsed)
        var format: SimpleDateFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        return format.format(date)
    }
}