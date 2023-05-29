package chat.simplex.common.platform

import androidx.compose.ui.graphics.*
import boofcv.core.image.ConvertImage
import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayU8
import java.io.ByteArrayOutputStream

actual fun base64ToBitmap(base64ImageString: String): ImageBitmap = TODO()
actual fun resizeImageToStrSize(image: ImageBitmap, maxDataSize: Long): String = TODO()
actual fun resizeImageToDataSize(image: ImageBitmap, usePng: Boolean, maxDataSize: Long): ByteArrayOutputStream = TODO()
actual fun cropToSquare(image: ImageBitmap): ImageBitmap = TODO()
actual fun compressImageStr(bitmap: ImageBitmap): String = TODO()
actual fun compressImageData(bitmap: ImageBitmap, usePng: Boolean): ByteArrayOutputStream = TODO()

actual fun GrayU8.toImageBitmap(): ImageBitmap = ConvertBufferedImage.extractBuffered(this).toComposeImageBitmap()
actual fun ImageBitmap.addLogo(): ImageBitmap = TODO()
actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap = TODO()