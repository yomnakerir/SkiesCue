package com.example.skiescue.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.skiescue.R
import com.example.skiescue.home.view.HomeFragment
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun getIconImage(icon: String): Int {
    val iconValue: Int
    when (icon) {
        "01d" -> iconValue = R.raw.sunny
        "01n" -> iconValue = R.raw.night
        "02d" -> iconValue = R.raw.sunclouds
        "02n" -> iconValue = R.raw.cloudynight
        "03n" -> iconValue = R.raw.clearclouds
        "03d" -> iconValue = R.raw.clearclouds
        "04d" -> iconValue = R.raw.brokenclod
        "04n" -> iconValue = R.raw.brokenclod
        "09d" -> iconValue = R.raw.darkrain
        "09n" -> iconValue = R.raw.darkrain
        "10d" -> iconValue = R.raw.cloudysunrain
        "10n" -> iconValue = R.raw.rainynight
        //"11d" -> iconValue = R.drawable.a11d
        //"11n" -> iconValue = R.drawable.a11n
        //"13d" -> iconValue = R.drawable.a13d
        //"13n" -> iconValue = R.drawable.a13n
        //"50d" -> iconValue = R.drawable.a50d
        //"50n" -> iconValue = R.drawable.a50n
        else -> iconValue = R.raw.error
    }
    return iconValue
}


// convert to hours
@SuppressLint("SimpleDateFormat")
fun convertToTime(dt: Long, context: Context): String {
    val date = Date(dt * 1000)
    val format = SimpleDateFormat("h:mm a", Locale(getCurrentLan(context)))
    return format.format(date)
}

fun timestampToReadableTime(dt:Long): String {
   return  SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(dt*1000))
  /*  return SimpleDateFormat("h:mm a",Locale.getDefault()).apply {
        timeZone.rawOffset = HomeFragment.timeOffestValue
    }.format(Date(dt*1000))
*/
}






fun convertToDate(dt: Long, context: Context): String {
    val date = Date(dt * 1000)
    val format = SimpleDateFormat("d / MM / yyyy", Locale(getCurrentLan(context)))
    return format.format(date)
}

fun convertToDateSecondForm(dt: Long, context: Context): String {
    val date = Date(dt * 1000)
    val format = SimpleDateFormat("d ", Locale(getCurrentLan(context)))
    return format.format(date)
}





fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun initSharedPref(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.shared_pref),
        Context.MODE_PRIVATE
    )

}

fun initFavSharedPref(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.shared_fav_pref),
        Context.MODE_PRIVATE
    )

}

fun checkSharedTimeZone(context: Context): Boolean {
    val sharedPref = initSharedPref(context)
    return sharedPref.getString(context.getString(R.string.TIMEZONE), "null").isNullOrEmpty()
}


fun checkShared(context: Context): Boolean {
    val sharedPref = initSharedPref(context)
    return sharedPref.getFloat(context.getString(R.string.LAT), 0f) == 0f
}


//
//// history of Days
fun convertLongToDay(time: Long, context: Context): String {
    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("EEE", Locale(getCurrentLan(context)))
    return format.format(date)
}

// for Lan
fun initLan(lan: String, context: Context) {
    initSharedPref(context).edit().apply {
        putString(context.getString(R.string.LAN), lan)
        apply()
    }
}

// for Unit
fun initUNIT(unit: String, context: Context) {
    initSharedPref(context).edit().apply {
        putString(context.getString(R.string.UNITS), unit)
        apply()
    }
}

// for choosing location way
fun initLocation(location: String, context: Context) {
    initSharedPref(context).edit().apply {
        putString(context.getString(R.string.LOCATIONCHOICE), location)
        apply()
    }
}

fun getCurrentLan(context: Context): String {
    return initSharedPref(context).getString(
        context.getString(R.string.LAN),
        Languages.ENGLISH.name
    ) ?: Languages.ENGLISH.name
}

fun getCurrentUnit(context: Context): String {
    return initSharedPref(context).getString(
        context.getString(R.string.UNITS),
        Units.METRIC.name
    ) ?: Units.METRIC.name
}

// getUnits
fun getCurrentTemperature(context: Context): String {
    return when (getCurrentUnit(context)) {
        Units.METRIC.name -> {
            context.getString(R.string.Celsusi)
        }
        Units.IMPERIAL.name -> {
            context.getString(R.string.Ferherhit)
        }
        Units.STANDARD.name -> {
            context.getString(R.string.KELVIN)
        }
        else -> {
            context.getString(R.string.Celsusi)
        }
    }
}

// get Speed
fun getCurrentSpeed(context: Context): String {

    return when (getCurrentUnit(context)) {

        Units.IMPERIAL.name -> {
            context.getString(R.string.MiliPerHour)
        }

        else -> {
            context.getString(R.string.MeterPerSecond)

        }
    }


}

//
fun convertNumbersToArabic(value: Double): String {
    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
}

fun convertNumbersToArabic(value: Int): String {
    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
}
