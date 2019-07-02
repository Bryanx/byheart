package com.example.byheart.shared

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.byheart.MainActivity
import com.example.byheart.R
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

fun Context.getAttr(id: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}