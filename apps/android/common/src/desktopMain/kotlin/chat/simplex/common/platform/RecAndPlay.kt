package chat.simplex.common.platform

import androidx.compose.runtime.MutableState
import chat.simplex.common.model.ChatItem
import kotlinx.coroutines.CoroutineScope

actual class RecorderNative: RecorderInterface {
  override fun start(onProgressUpdate: (position: Int?, finished: Boolean) -> Unit): String {
    TODO("Not yet implemented")
  }

  override fun stop(): Int {
    TODO("Not yet implemented")
  }
}

actual object AudioPlayer: AudioPlayerInterface {
  override fun play(filePath: String?, audioPlaying: MutableState<Boolean>, progress: MutableState<Int>, duration: MutableState<Int>, resetOnEnd: Boolean) {
    TODO("Not yet implemented")
  }

  override fun stop() {
    TODO("Not yet implemented")
  }

  override fun stop(item: ChatItem) {
    TODO("Not yet implemented")
  }

  override fun stop(fileName: String?) {
    TODO("Not yet implemented")
  }

  override fun pause(audioPlaying: MutableState<Boolean>, pro: MutableState<Int>) {
    TODO("Not yet implemented")
  }

  override fun seekTo(ms: Int, pro: MutableState<Int>, filePath: String?) {
    TODO("Not yet implemented")
  }

  override fun duration(filePath: String): Int? {
    TODO("Not yet implemented")
  }
}

actual object SoundPlayer: SoundPlayerInterface {
  override fun start(scope: CoroutineScope, sound: Boolean) = TODO()
  override fun stop() = TODO()
}