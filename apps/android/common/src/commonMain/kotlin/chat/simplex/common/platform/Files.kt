package chat.simplex.common.platform

import chat.simplex.common.model.CIFile
import java.io.File

expect val dataDir: File

fun getFilesDirectory(): String {
  return dataDir.absolutePath + File.separator + "files"
}

// LALAL
fun getTempFilesDirectory(): String {
  return getFilesDirectory() + File.separator + "temp_files"
}

fun getAppFilesDirectory(): String {
  return getFilesDirectory() + File.separator + "app_files"
}

fun getAppFilePath(fileName: String): String {
  return getAppFilesDirectory() + File.separator + fileName
}

fun getLoadedFilePath(file: CIFile?): String? {
  return if (file?.filePath != null && file.loaded) {
    val filePath = getAppFilePath(file.filePath)
    if (File(filePath).exists()) filePath else null
  } else {
    null
  }
}