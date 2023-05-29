package chat.simplex.common.platform


const val TAG = "SIMPLEX"

actual object Log {
  actual fun d(tag: String, text: String) = println("DEBUG: $text")
  actual fun e(tag: String, text: String) = println("ERROR: $text")
  actual fun i(tag: String, text: String) = println("INFO: $text")
  actual fun w(tag: String, text: String) = println("WARNING: $text")
}