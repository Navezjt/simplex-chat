package chat.simplex.common.platform

import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

actual fun copyText(text: String) {
  LocalClipboardManager.current.setText(AnnotatedString(text))
}

actual fun sendEmail(subject: String, body: CharSequence) {

}

actual fun shareText(text: String) {
  copyText(text)
}