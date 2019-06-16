package com.example.byheart.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// Easily inflate view groups
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
