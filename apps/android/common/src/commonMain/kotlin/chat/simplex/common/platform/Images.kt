package chat.simplex.common.platform

import androidx.compose.ui.graphics.ImageBitmap
import boofcv.struct.image.GrayU8
import java.io.ByteArrayOutputStream

expect fun base64ToBitmap(base64ImageString: String): ImageBitmap
expect fun resizeImageToStrSize(image: ImageBitmap, maxDataSize: Long): String
expect fun resizeImageToDataSize(image: ImageBitmap, usePng: Boolean, maxDataSize: Long): ByteArrayOutputStream
expect fun cropToSquare(image: ImageBitmap): ImageBitmap
expect fun compressImageStr(bitmap: ImageBitmap): String
expect fun compressImageData(bitmap: ImageBitmap, usePng: Boolean): ByteArrayOutputStream

expect fun GrayU8.toImageBitmap(): ImageBitmap

expect fun ImageBitmap.addLogo(): ImageBitmap
expect fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap