package chat.simplex.common.platform

actual val cryptor: CryptorInterface = object : CryptorInterface {
  override fun decryptData(data: ByteArray, iv: ByteArray, alias: String): String? {
    TODO("Not yet implemented")
  }

  override fun encryptText(text: String, alias: String): Pair<ByteArray, ByteArray> {
    TODO("Not yet implemented")
  }

  override fun deleteKey(alias: String) {
    TODO("Not yet implemented")
  }
}
