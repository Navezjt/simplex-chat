package chat.simplex.common.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.russhwolf.settings.*
import java.util.*

@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font =
  androidx.compose.ui.text.platform.Font("font/$res.ttf", weight, style)

actual fun isInNightMode() = false

// LALAL
actual val settings: Settings = PropertiesSettings(Properties())
actual val settingsThemes: Settings = PropertiesSettings(Properties())

actual fun screenOrientation(): ScreenOrientation = ScreenOrientation.UNDEFINED

@Composable // LALAL
actual fun screenWidthDp(): Int = 1000000