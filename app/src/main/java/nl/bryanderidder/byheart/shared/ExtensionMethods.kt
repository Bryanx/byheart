package nl.bryanderidder.byheart.shared

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_rehearsal_result.*
import nl.bryanderidder.byheart.MainActivity
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.pile.edit.ColorStateDrawable
import java.lang.Integer.parseInt
import java.util.*

/**
 * Contains all extension methods.
 * @author Bryan de Ridder
 */

// Easily inflate view groups
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

// Start fragment in pile_menu container
fun FragmentManager.startFragment(fragment: Fragment) {
    val tag = fragment::class.java.canonicalName
    this.beginTransaction()
        .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
        .replace(R.id.main_container, fragment, tag)
        .commit()
}

// Start fragment in pile_menu container
fun Fragment.startFragment(fragment: Fragment) {
    this.fragmentManager?.beginTransaction()
        ?.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
        ?.replace(R.id.main_container, fragment)
        ?.commit()
}

// Do something on animation end
fun Animation.onAnimateEnd(args: () -> Unit) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(arg0: Animation) {}
        override fun onAnimationEnd(animation: Animation) { args() }
        override fun onAnimationRepeat(animation: Animation) {}
    })
}

// Adds a toolbar to a fragment
fun Fragment.addToolbar(
    hasBackButton: Boolean = true,
    hasOptions: Boolean = true,
    title: String = ""
) {
    val mainAct = (activity as MainActivity)
    mainAct.supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(hasBackButton)
        setDisplayShowHomeEnabled(hasBackButton)
        this.title = title
        show()
    }
    this.setHasOptionsMenu(hasOptions)
    if (hasBackButton) mainAct.toolbar.setNavigationOnClickListener { mainAct.onBackPressed() }
}

// Returns an attribute
fun Context.getAttr(id: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

// Darkens an int color by a certain factor
fun Int.setBrightness(factor: Float): Int {
    val hexWithAlpha = Integer.toHexString(this)
    val hex = hexWithAlpha.substring(2, hexWithAlpha.length)
    val hsl = floatArrayOf(0F,0F,0F)
    ColorUtils.RGBToHSL(parseInt(hex.substring( 0, 2 ), 16),
        parseInt(hex.substring( 2, 4 ), 16),
        parseInt(hex.substring( 4, 6 ), 16),
        hsl)
    hsl[2] = factor
    return ColorUtils.HSLToColor(hsl)
}

// Darkens an int color by a certain factor
fun Int.setAlpha(factor: Float): Int = ColorUtils.setAlphaComponent(this, (factor * 255).toInt())

fun FloatingActionButton.setColor(color: Int) {
    this.backgroundTintList = ColorStateList.valueOf(color)
}

fun FloatingActionButton.setPureColor(color: Int) {
    this.backgroundTintList = ColorStateList.valueOf(color)
}

fun ImageView.setTint(id: Int, blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    this.setColorFilter(id, blendMode)
}


fun ImageView.setTintColor(color: Int, blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    this.setColorFilter(color, blendMode)
}

fun Button.setBackgroundTint(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = ColorStateList.valueOf(color)
    }
}

//Change color of drawable for imageview
fun ImageView.setDrawableColor(drawableId: Int, color: Int) {
    return this.setImageDrawable(
        ColorStateDrawable(
            arrayOf(this.context.resources.getDrawable(drawableId)),
            color)
    )
}
@SuppressLint("UseCompatLoadingForDrawables")
fun ImageView.setDrawable(drawableId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        setImageDrawable(resources.getDrawable(drawableId, context!!.theme))
    else
        setImageDrawable(resources.getDrawable(drawableId))
}

fun Timer.scheduleAtFixedRate(delay: Long, period: Long, onTick: () -> Unit) {
    scheduleAtFixedRate(object : TimerTask() {
        override fun run() { onTick() }
    }, delay, period)
}

fun FloatingActionButton.setIconColor(color: Int) {
    this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

fun Activity?.setStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = (this as MainActivity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.color(color)
    }
}

// Returns a color
fun Context.color(id: Int): Int {
    return ContextCompat.getColor(this, id)
}

// Returns a styled alert dialog builder
fun Context.dialog(): AlertDialog.Builder = when {
    Preferences.DARK_MODE -> AlertDialog.Builder(this, R.style.DarkDialogTheme)
    else -> AlertDialog.Builder(this)
}

fun AlertDialog.Builder.setAnimation(styleId: Int): AlertDialog {
    val dialog = this.create()
    dialog.window?.attributes?.windowAnimations = styleId
    return dialog
}

fun TextToSpeech.pronounce(text: String?) = this.speak(text, TextToSpeech.QUEUE_FLUSH, null)

// get menu item name
fun MenuItem.getId(res: Resources): String =
    if (this.itemId == -0x1) "no-id"
    else res.getResourceEntryName(itemId) ?: "error-getting-id"


fun ObjectAnimator.onAnimateEnd(args: () -> Unit) {
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) { args() }
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
}

fun String.equalsIgnoreCase(string: String): Boolean {
    return this.toLowerCase() == string.toLowerCase()
}

fun String.getExtension(): String {
    return this.substring(this.lastIndexOf(".")+1)
}

fun EditText.setLineColor(color: Int) {
    if (Build.VERSION.SDK_INT >= 21) this.backgroundTintList = ColorStateList.valueOf(color)
}

fun Activity?.hideKeyboard() {
    val inputManager = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun EditText.onEnter(action: () -> Boolean) {
    this.setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                return action()
            }
            return false
        }
    })
}

fun EditText.showKeyboard() {
    post {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun EditText.setCaretColor(color: Int) {
    try {
        // Change cursor color
        val unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.cursor)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, color)
        // Get the cursor resource id
        val field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        field.isAccessible = true
        field.set(this, R.drawable.cursor)
    } catch (e: Exception) {
    }
}

operator fun Any?.get(name: String): Any? {
    return this?.javaClass?.getMethod("get${name.capitalize()}")?.invoke(this)
}

fun Any.getAttr(name: String): Any? {
    return this.javaClass.getMethod("get${name.initCaps()}").invoke(this)
}

fun String.initCaps(): String {
    return this[0].toUpperCase() + this.substring(1, this.length)
}

fun ProgressBar.setColor(@ColorInt color: Int = ContextCompat.getColor(context, R.color.colorPrimary)) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        indeterminateTintList = ColorStateList.valueOf(color)
    } else {
        (indeterminateDrawable as? LayerDrawable)?.apply {
            if (numberOfLayers >= 2) {
                setId(0, android.R.id.progress)
                setId(1, android.R.id.secondaryProgress)
                val progressDrawable = findDrawableByLayerId(android.R.id.progress).mutate()
                progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
}

// property extensions

// get viewname
val View.name: String get() =
    if (this.id == -0x1) "no-id"
    else resources.getResourceEntryName(id) ?: "error-getting-name"

val TextView.string: String get() = this.text.toString()
val TextView.long: Long get() = this.text.toString().toLong()

val ImageView.tint: ColorFilter get() = this.colorFilter

val Locale.code: String get() = this.language + "-" + this.country

var SwitchMaterial.checkedColor: Int
    get() = thumbTintList!!.defaultColor
    set(color) {
        if (isChecked) {
            thumbTintList = ColorStateList.valueOf(color)
            trackTintList = ColorStateList.valueOf(color.setAlpha(0.15F))
        }
    }

var SwitchMaterial.unCheckedColor: Int
    get() = thumbTintList!!.defaultColor
    set(color) {
        if (!isChecked) {
            thumbTintList = ColorStateList.valueOf(color)
            trackTintList = if (Preferences.DARK_MODE) ColorStateList.valueOf(color.setAlpha(0.15F))
            else ColorStateList.valueOf(color.setAlpha(0.35F))
        }
    }