package chat.simplex.common.views.chat

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import com.icerockdev.library.MR
import chat.simplex.common.ui.theme.DEFAULT_PADDING
import chat.simplex.common.views.helpers.*
import chat.simplex.common.views.newchat.QRCodeScanner
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun ScanCodeView(verifyCode: (String?, cb: (Boolean) -> Unit) -> Unit, close: () -> Unit) {
  val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
  LaunchedEffect(Unit) {
    cameraPermissionState.launchPermissionRequest()
  }
  ScanCodeLayout(verifyCode, close)
}

@Composable
private fun ScanCodeLayout(verifyCode: (String?, cb: (Boolean) -> Unit) -> Unit, close: () -> Unit) {
  Column(
    Modifier
      .fillMaxSize()
      .padding(horizontal = DEFAULT_PADDING)
  ) {
    AppBarTitle(stringResource(MR.strings.scan_code), false)
    Box(
      Modifier
        .fillMaxWidth()
        .aspectRatio(ratio = 1F)
        .padding(bottom = DEFAULT_PADDING)
    ) {
      QRCodeScanner { text ->
        verifyCode(text) {
          if (it) {
            close()
          } else {
            AlertManager.shared.showAlertMsg(
              title = generalGetString(MR.strings.incorrect_code)
            )
          }
        }
      }
    }
    Text(stringResource(MR.strings.scan_code_from_contacts_app))
  }
}
