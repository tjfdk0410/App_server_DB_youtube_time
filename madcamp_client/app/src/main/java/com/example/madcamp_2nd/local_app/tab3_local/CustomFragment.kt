package com.example.madcamp_2nd.local_app.tab3_local

import android.Manifest
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.PendingIntent.getActivity
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.AppOpsManagerCompat
import androidx.core.content.ContextCompat.*

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.contacts.*
import kotlinx.android.synthetic.main.custom.*
import java.text.SimpleDateFormat
import java.util.*
import android.R
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.gallery.*


class CustomFragment: Fragment() {

//    lateinit var UsageState: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(com.example.madcamp_2nd.R.layout.custom, container, false) //fragement 생성 위한 view를 custom에서 띄우고 반환
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /**************로그인 없으면 기능 막기******************
        custom_activity_button.setOnClickListener {
            val intent = Intent(context, CustomActivity::class.java)
            startActivity(intent)
        }
        ********************************************************/
    }

}





//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.custom)

//        UsageState = findViewById(R.id.UsageState)

        /*****************
        //private
        fun convertTime1(lastTimeUsed: Long): String{
            var date: Date = Date(lastTimeUsed)
            var format : SimpleDateFormat = SimpleDateFormat ("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
            return format.format(date)
        }

        //private
        fun convertTime2(lastTimeUsed: Long): String{
            var date: Date = Date(lastTimeUsed)
            var format : SimpleDateFormat = SimpleDateFormat ("hh:mm", Locale.ENGLISH)
            return format.format(date)
        }

        //private
        fun showUsageStats(){
            var usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            var cal: Calendar = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_MONTH, -1)
            var queryUsageStats : List<UsageStats> = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,cal.timeInMillis, System.currentTimeMillis())
            var stats_data : String = ""
            for(i in 0..queryUsageStats.size-1){
                stats_data = stats_data + "Package Name : " + queryUsageStats.get(i).packageName + "\n" +
                        "Last Time Used : " + convertTime1(queryUsageStats.get(i).lastTimeUsed) + "\n" +
                        "Describe Contents : " + queryUsageStats.get(i).describeContents() + "\n" +
                        "First Time Stamp : " + convertTime1(queryUsageStats.get(i).firstTimeStamp) + "\n" +
                        "Last Time Stamp : " + convertTime1(queryUsageStats.get(i).lastTimeStamp) + "\n" +
                        "Total Time in Foreground : " + convertTime2(queryUsageStats.get(i).totalTimeInForeground) + "\n\n"
            }
            UsageState.setText(stats_data)
        }

        showUsageStats()
        }
        ******************************/


//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        return super.onCreateView(name, context, attrs)
//    }
    /*********
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.custom, container, false)
        return view
    }
    **********/

