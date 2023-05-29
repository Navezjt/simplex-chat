package chat.simplex.common.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import chat.simplex.common.platform.font

// https://github.com/rsms/inter
val Inter = FontFamily(
  font("inter_regular", "inter_regular"),
  font("inter_italic", "inter_italic", style = FontStyle.Italic),
  font("inter_bold", "inter_bold", weight = FontWeight.Bold),
  font("inter_semi_bold", "inter_semi_bold", weight = FontWeight.SemiBold),
  font("inter_medium", "inter_medium", weight = FontWeight.Medium),
  font("inter_light", "inter_light", weight = FontWeight.Light),
)

// Set of Material typography styles to start with
val Typography = Typography(
  h1 = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
  ),
  h2 = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp
  ),
  h3 = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 18.5.sp
  ),
  h4 = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 17.5.sp
  ),
  body1 = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
  ),
  body2 = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp
  ),
  button = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
  ),
  caption = TextStyle(
    fontFamily = Inter,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp
  )
)
