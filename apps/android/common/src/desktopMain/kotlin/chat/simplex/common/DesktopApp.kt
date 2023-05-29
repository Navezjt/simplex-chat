package chat.simplex.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Preview
@Composable
fun AppPreview() {
  App()
}

/** Needed for [chat.simplex.common.platform.Files] to get path to jar file */
class DesktopApp()