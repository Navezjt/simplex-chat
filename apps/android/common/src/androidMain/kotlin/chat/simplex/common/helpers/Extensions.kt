package chat.simplex.common.helpers

import android.os.Build
import chat.simplex.common.BuildConfig
import chat.simplex.common.BuildConfig.LIBRARY_PACKAGE_NAME
import chat.simplex.common.model.NotificationsMode

val NotificationsMode.requiresIgnoringBatterySinceSdk: Int get() = when(this) {
  NotificationsMode.OFF -> Int.MAX_VALUE
  NotificationsMode.PERIODIC -> Build.VERSION_CODES.M
  NotificationsMode.SERVICE -> Build.VERSION_CODES.S
  /*INSTANT -> Int.MAX_VALUE - for Firebase notifications */
}

val NotificationsMode.requiresIgnoringBattery
  get() = requiresIgnoringBatterySinceSdk <= Build.VERSION.SDK_INT

val APPLICATION_ID: String
  get() = LIBRARY_PACKAGE_NAME.replace("common", "app")