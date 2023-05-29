package chat.simplex.common.platform

expect fun copyText(text: String)
expect fun sendEmail(subject: String, body: CharSequence)
// LALAL
expect fun shareText(text: String)