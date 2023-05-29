package chat.simplex.common.platform

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import chat.simplex.common.views.helpers.KeyboardState

actual fun showToast(text: String) {

}

@Composable
actual fun LockToCurrentOrientationUntilDispose() {}

@Composable
actual fun LocalMultiplatformView(): Any? = null

@Composable
actual fun getKeyboardState(): State<KeyboardState> = remember { mutableStateOf(KeyboardState.Opened) }
actual fun hideKeyboard(view: Any?) {
  LocalSoftwareKeyboardController.current?.hide()
}