package com.example.helloworld.firebase
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.helloworld.R
import com.example.helloworld.activities.HomePageActivity
import com.example.helloworld.activities.IntroActivity
import com.example.helloworld.activities.MyMessage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.eazyalgo.fcmpushotification"

class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage){

        if(remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }

        @SuppressLint("RemoteViewLayout")
        fun getRemoteView(title: String, message: String) : RemoteViews {
            val remoteView = RemoteViews("com.example.helloworld.firebase", R.layout.notification)
            remoteView.setTextViewText(R.id.title,title)
            remoteView.setTextViewText(R.id.message,message)
            remoteView.setImageViewResource(R.id.icon, R.drawable.ic_pmain)

            return remoteView
        }

            @SuppressLint("UnspecifiedImmutableFlag")
            fun generateNotification(title: String, message: String) {

                val intent = Intent(this, HomePageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

            var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_pmain)
                .setAutoCancel(true).setVibrate(longArrayOf(1000,1000,1000,1000))
                .setOnlyAlertOnce(true).setContentIntent(pendingIntent)

            builder = builder.setContent(getRemoteView(title,message))

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(notificationChannel)
                }
                notificationManager.notify(0,builder.build())

        }


}