package android.dwkim.pr.gotothesugarhill

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.IBinder
import android.arch.persistence.room.Room
import android.dwkim.pr.gotothesugarhill.db.Arrive
import android.dwkim.pr.gotothesugarhill.db.ArrivesDB
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import android.app.PendingIntent
import android.support.v4.app.TaskStackBuilder
import android.app.NotificationChannel




class WifiCatchService : Service() {
    override fun onCreate() {
        super.onCreate()

        Log.d("@@@@@", "create Service")
        ConnectionStateMonitor(applicationContext)
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("@@@@@", "bind Service")
        return null
    }

    inner class ConnectionStateMonitor(private val context: Context) : ConnectivityManager.NetworkCallback() {
        init {
            val networkRequest =
                    NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .build()

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(networkRequest, this)
        }

        override fun onAvailable(network: Network) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            info?.run {
                if(this.isConnected){
                    val wifiName = info.extraInfo.replace("\"", "")
                    Log.d("@@@@@", "available $wifiName")
                    if(wifiName == "KT_GiGA_5G_D6F7" || wifiName == "KT_GiGA_5G_7FB3"){
                        val item = Arrive(0, Date(), wifiName)

                        addDB(item)
                        createNotification(item)
                    }
                }
            }

            super.onAvailable(network)
        }

        private fun addDB(item: Arrive){
            val dao =
                    Room.databaseBuilder(
                            applicationContext,
                            ArrivesDB::class.java,
                            "ArrivesDB").build()

            dao.arriveDao().insert(item)
        }

        private fun createNotification(item: Arrive){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationId = 1
            val channelId = "channel01"
            val channelName = "Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(
                        NotificationChannel(channelId, channelName, importance)
                )
            }

            val mBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("출근 알림")
                    .setContentText("${SimpleDateFormat("HH:mm:ss", Locale.KOREA).format(item.date)} / ${item.wifiName}")

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntent(Intent())
            val resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            mBuilder.setContentIntent(resultPendingIntent)

            notificationManager.notify(notificationId, mBuilder.build())
        }
    }
}