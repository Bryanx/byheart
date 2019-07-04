package com.example.byheart.shared

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.shared.Preferences.DARK_MODE
import kotlinx.android.synthetic.main.activity_main.*

// Easily inflate view groups
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

// Start fragment in pile_menu container
fun FragmentManager.startFragment(fragment: Fragment) {
    this.beginTransaction()
        .replace(R.id.main_container, fragment)
        .commit()
}

// Do something on animation end
fun Animation.onAnimateEnd(args: () -> Unit) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(arg0: Animation) {}
        override fun onAnimationEnd(animation: Animation) { args() }
        override fun onAnimationRepeat(animation: Animation) {}
    })
}

// Brings an edit text into focus
fun EditText.focus() {
    this.requestFocus()
    val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

// Adds a toolbar to a fragment
fun Fragment.addToolbar(
    activity: Activity,
    hasBackButton: Boolean,
    title: String,
    hasOptions: Boolean,
    onNavClick: (() -> Unit)?
) {
    val mainAct = (activity as MainActivity)
    mainAct.supportActionBar?.setDisplayHomeAsUpEnabled(hasBackButton)
    mainAct.supportActionBar?.setDisplayShowHomeEnabled(hasBackButton)
    mainAct.supportActionBar?.title = title
    this.setHasOptionsMenu(hasOptions)
    if (onNavClick != null) {
        activity.toolbar.setNavigationOnClickListener { onNavClick() }
    }
}

// Returns an attribute
fun Context.getAttr(id: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

fun ImageView.setTint(id: Int, blendMode: PorterDuff.Mode) {
    this.setColorFilter(context!!.color(id), blendMode)
}

// Returns a color
fun Context.color(id: Int): Int {
    return ContextCompat.getColor(this, id)
}

// Returns a styled alert dialog builder
fun Context.dialog(): AlertDialog.Builder = when {
    Preferences.read(DARK_MODE) -> AlertDialog.Builder(this, R.style.DarkDialogTheme)
    else -> AlertDialog.Builder(this)
}

fun TextToSpeech.pronounce(text: String?) = this.speak(text, TextToSpeech.QUEUE_FLUSH, null)

// get menu item name
fun MenuItem.getName(res: Resources): String =
    if (this.itemId == -0x1) "no-id"
    else res.getResourceEntryName(itemId) ?: "error-getting-name"


fun ObjectAnimator.onAnimateEnd(args: () -> Unit) {
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) { args() }
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
}

// property extensions

// get viewname
val View.name: String get() =
    if (this.id == -0x1) "no-id"
    else resources.getResourceEntryName(id) ?: "error-getting-name"

val TextView.string: String get() = this.text.toString()

val ImageView.tint: ColorFilter get() = this.colorFilter
