package com.dasherprofitbot.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat

class OfferNotificationListener : NotificationListenerService() {
 override fun onNotificationPosted(sbn: StatusBarNotification?) {
  if (sbn == null || sbn.packageName == packageName) return
  val platform = when(sbn.packageName) {
   "com.doordash.driverapp" -> "DoorDash"
   "com.ubercab.driver" -> "Uber Eats"
   else -> return
  }
  val prefs = getSharedPreferences("offer_settings", MODE_PRIVATE)
  if (!prefs.getBoolean("enabled", false)) return
  val extras = sbn.notification.extras
  val message = listOf(
   extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty(),
   extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty(),
   extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString().orEmpty()
  ).joinToString(" ")
  val pay = Regex("""\$\s*(\d+(?:\.\d{1,2})?)""").find(message)?.groupValues?.get(1)?.toDoubleOrNull() ?: return
  val miles = Regex("""(\d+(?:\.\d+)?)\s*(?:mi\b|miles?\b)""", RegexOption.IGNORE_CASE).find(message)?.groupValues?.get(1)?.toDoubleOrNull() ?: return
  if (miles <= 0) return
  val min = prefs.getFloat("minimum", 3f).toDouble()
  val max = prefs.getFloat("maximum", 1f).toDouble()
  val ratio = prefs.getFloat("ratio", 2f).toDouble()
  if (pay < min || miles > max || pay / miles < ratio) return
  val manager = getSystemService(NotificationManager::class.java)
  val channel = "profitable_offers"
  manager.createNotificationChannel(NotificationChannel(channel, "Ofertas rentables", NotificationManager.IMPORTANCE_HIGH))
  val intent = Intent(this, MainActivity::class.java)
  val pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
  val alert = NotificationCompat.Builder(this, channel)
   .setSmallIcon(android.R.drawable.ic_dialog_info)
   .setContentTitle("$platform: oferta que cumple filtros")
   .setContentText("$" + "%.2f".format(pay) + " · " + "%.1f".format(miles) + " mi · $" + "%.2f".format(pay/miles) + "/mi")
   .setContentIntent(pending).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
   .setDefaults(NotificationCompat.DEFAULT_ALL).build()
  manager.notify((sbn.key.hashCode() and 0x7fffffff), alert)
 }
}
