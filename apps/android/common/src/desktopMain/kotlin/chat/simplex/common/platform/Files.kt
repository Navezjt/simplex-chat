package chat.simplex.common.platform

import chat.simplex.common.DesktopApp
import java.io.File

private fun applicationParentPath(): String = try {
  DesktopApp::class.java.protectionDomain!!.codeSource.location.toURI().path
    .replaceAfterLast("/", "")
    .replaceAfterLast(File.separator, "")
    .replace("/", File.separator)
} catch (e: Exception) {
  "./"
}

// LALAL
actual val dataDir: File = File(applicationParentPath())