package com.example.byheart.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.byheart.R

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
        override fun onAnimationEnd(animation: Animation) {
            args()
        }
        override fun onAnimationRepeat(animation: Animation) {}
    })
}