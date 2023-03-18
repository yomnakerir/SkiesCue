package com.example.skiescue.alert.view
import android.app.*
import android.content.ContentResolver
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.skiescue.R
import com.example.skiescue.home.view.MainActivity
import android.media.RingtoneManager
import android.net.Uri


private const val CHANNEL_ID = 1
private const val FOREGROUND_ID = 2


class AlertService : Service( ) {


    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertWindowManger? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Get Data from Work Manager
        val description = intent?.getStringExtra("description")

        // Create Notification Channel
        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification(description?:""))

        //start window manger
        if (Settings.canDrawOverlays(this)) {
            alertWindowManger = AlertWindowManger(this, description?:"")
            alertWindowManger!!.initializeWindowManager()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(description: String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return NotificationCompat.Builder(applicationContext, "$CHANNEL_ID")
            .setContentText(description)
            .setContentTitle("SkiesCue Alarm")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setSmallIcon(R.drawable.icond_weather)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .build()
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String = "SkiesCue Notification"
            val description = "Alert Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("$CHANNEL_ID",name, importance)
            channel.enableVibration(true)



               /* val sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.song)
                val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                channel.enableVibration(true)
               channel.setSound(sound, attributes)*/


            channel.description = description
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


}