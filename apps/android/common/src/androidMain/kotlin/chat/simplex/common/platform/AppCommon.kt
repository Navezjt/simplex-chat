package chat.simplex.common.platform

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference
import java.util.*

var isAppOnForeground: Boolean = false

@SuppressLint("ConstantLocale")
val defaultLocale: Locale = Locale.getDefault()

@SuppressLint("StaticFieldLeak")
lateinit var androidAppContext: Context
lateinit var mainActivity: WeakReference<FragmentActivity>

lateinit var serviceStart: suspend () -> Unit
