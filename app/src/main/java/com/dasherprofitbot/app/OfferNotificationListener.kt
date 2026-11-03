package com.dasherprofitbot.app

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * Opt-in notification access foundation.
 * No credentials, notification content, or personal data are stored or transmitted.
 * Offer parsing is intentionally disabled until supported notification formats
 * and user-facing consent controls have been validated.
 */
class OfferNotificationListener : NotificationListenerService() {
 override fun onNotificationPosted(sbn: StatusBarNotification?) {
  // Future: locally parse explicitly supported offer notifications only.
 }
}
