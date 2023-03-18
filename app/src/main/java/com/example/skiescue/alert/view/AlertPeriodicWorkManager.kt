package com.example.skiescue.alert.view

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.skiescue.model.Repository
import androidx.work.*
import com.example.skiescue.model.getCurrentLan
import com.example.skiescue.model.getCurrentUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "AlertPeriodicWorkManage"
class AlertPeriodicWorkManager(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    // intialize repo
    val repository = Repository(context)

    override suspend fun doWork(): Result {
        if (!isStopped) {

                val id = inputData.getLong("id", -1)
                Log.i(TAG, "getData: aler data "+id)

                getData(id)





        }
        return Result.success()
    }

    private suspend fun getData( id:Long) {
        // request data from room or network
        val alert = repository.getAlert(id.toInt())
        Log.i(TAG, "getData: aler data "+alert)

        repository.getWeatherDetalis(alert.latitude?:0.0,alert.longitude?:0.0, getCurrentLan(context), getCurrentUnit(context))
            .collect{


                if (checkTimeLimit(alert)) {
                    val delay: Long = getDelay(alert)
                    // currentWeather?.alerts.isNullOrEmpty()
                    if (it.alerts.isNullOrEmpty()) {
                        setOneTimeWorkManger(
                            delay,
                            alert.id, it.current?.weather?.get(0)?.description ?: "Weather is fine"
                        )
                    } else {
                        setOneTimeWorkManger(
                            delay,
                            alert.id,
                            description = it.alerts?.get(0)?.description?:"Weather is fine",
                        )
                    }
                } else {
                    repository.deleteAlert(alert.id?:-1)
                    WorkManager.getInstance().cancelAllWorkByTag("$id")
                }
        }

    }

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String) {
        val data = Data.Builder()
        data.putString("description", description)


        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()


        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManager::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    private fun getDelay(alert: AlertModel): Long {
        val hour = TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute = TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime!! - ((hour + minute) - (2 * 3600L))
    }

    private fun checkTimeLimit(alert: AlertModel): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date,context)

        return dayNow >= alert.startDate!!
                &&
                dayNow <= alert.endDate!!
    }


}