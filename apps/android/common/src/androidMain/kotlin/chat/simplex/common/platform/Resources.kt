package chat.simplex.common.platform

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import chat.simplex.common.model.AppPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

@SuppressLint("DiscouragedApi")
@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font {
  val context = LocalContext.current
  val id = context.resources.getIdentifier(res, "font", context.packageName)
  return Font(id, weight, style)
}

actual fun isInNightMode() =
  (androidAppContext.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager).nightMode == UiModeManager.MODE_NIGHT_YES

private val sharedPreferences: SharedPreferences = androidAppContext.getSharedPreferences(AppPreferences.SHARED_PREFS_ID, Context.MODE_PRIVATE)
private val sharedPreferencesThemes: SharedPreferences = androidAppContext.getSharedPreferences(AppPreferences.SHARED_PREFS_THEMES_ID, Context.MODE_PRIVATE)

actual val settings: Settings = SharedPreferencesSettings(sharedPreferences)
actual val settingsThemes: Settings = SharedPreferencesSettings(sharedPreferencesThemes)

actual fun screenOrientation(): ScreenOrientation = when (mainActivity.get()?.resources?.configuration?.orientation) {
  Configuration.ORIENTATION_PORTRAIT -> ScreenOrientation.PORTRAIT
  Configuration.ORIENTATION_LANDSCAPE -> ScreenOrientation.LANDSCAPE
  else -> ScreenOrientation.UNDEFINED
}

@Composable
actual fun screenWidthDp(): Int = LocalConfiguration.current.screenWidthDp