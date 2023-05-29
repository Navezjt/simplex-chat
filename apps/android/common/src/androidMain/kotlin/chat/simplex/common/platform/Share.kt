package chat.simplex.common.platform

import android.Manifest
import android.content.*
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import chat.simplex.common.BuildConfig
import chat.simplex.common.helpers.APPLICATION_ID
import chat.simplex.common.model.CIFile
import chat.simplex.common.views.helpers.generalGetString
import java.io.BufferedOutputStream
import java.io.File
import com.icerockdev.library.MR

actual fun shareText(text: String) {
  val sendIntent: Intent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, text)
    type = "text/plain"
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
  }
  val shareIntent = Intent.createChooser(sendIntent, null)
  androidAppContext.startActivity(shareIntent)
}

fun shareFile(text: String, filePath: String) {
  val uri = FileProvider.getUriForFile(androidAppContext, "$APPLICATION_ID.provider", File(filePath))
  val ext = filePath.substringAfterLast(".")
  val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: return
  val sendIntent: Intent = Intent().apply {
    action = Intent.ACTION_SEND
    /*if (text.isNotEmpty()) {
      putExtra(Intent.EXTRA_TEXT, text)
    }*/
    putExtra(Intent.EXTRA_STREAM, uri)
    type = mimeType
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
  }
  val shareIntent = Intent.createChooser(sendIntent, null)
  androidAppContext.startActivity(shareIntent)
}

actual fun copyText(text: String) {
  val clipboard = ContextCompat.getSystemService(androidAppContext, ClipboardManager::class.java)
  clipboard?.setPrimaryClip(ClipData.newPlainText("text", text))
}

actual fun sendEmail(subject: String, body: CharSequence) {
  val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
  emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
  emailIntent.putExtra(Intent.EXTRA_TEXT, body)
  emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
  try {
    androidAppContext.startActivity(emailIntent)
  } catch (e: ActivityNotFoundException) {
    Log.e(TAG, "No activity was found for handling email intent")
  }
}

@Composable
fun rememberSaveFileLauncher(ciFile: CIFile?): ManagedActivityResultLauncher<String, Uri?> =
  rememberLauncherForActivityResult(
    contract = ActivityResultContracts.CreateDocument(),
    onResult = { destination ->
      destination?.let {
        val filePath = getLoadedFilePath(ciFile)
        if (filePath != null) {
          val contentResolver = androidAppContext.contentResolver
          contentResolver.openOutputStream(destination)?.let { stream ->
            val outputStream = BufferedOutputStream(stream)
            File(filePath).inputStream().use { it.copyTo(outputStream) }
            outputStream.close()
            showToast(generalGetString(MR.strings.file_saved))
          }
        } else {
          showToast(generalGetString(MR.strings.file_not_found))
        }
      }
    }
  )

fun imageMimeType(fileName: String): String {
  val lowercaseName = fileName.lowercase()
  return when {
    lowercaseName.endsWith(".png") -> "image/png"
    lowercaseName.endsWith(".gif") -> "image/gif"
    lowercaseName.endsWith(".webp") -> "image/webp"
    lowercaseName.endsWith(".avif") -> "image/avif"
    lowercaseName.endsWith(".svg") -> "image/svg+xml"
    else -> "image/jpeg"
  }
}

/** Before calling, make sure the user allows to write to external storage [Manifest.permission.WRITE_EXTERNAL_STORAGE] */
fun saveImage(ciFile: CIFile?) {
  val filePath = getLoadedFilePath(ciFile)
  val fileName = ciFile?.fileName
  if (filePath != null && fileName != null) {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.MIME_TYPE, imageMimeType(fileName))
    values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
    values.put(MediaStore.MediaColumns.TITLE, fileName)
    val uri = androidAppContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    uri?.let {
      androidAppContext.contentResolver.openOutputStream(uri)?.let { stream ->
        val outputStream = BufferedOutputStream(stream)
        File(filePath).inputStream().use { it.copyTo(outputStream) }
        outputStream.close()
        showToast(generalGetString(MR.strings.image_saved))
      }
    }
  } else {
    showToast(generalGetString(MR.strings.file_not_found))
  }
}
