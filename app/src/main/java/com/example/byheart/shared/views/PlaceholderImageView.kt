package com.example.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.example.byheart.R
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.DARK_MODE


class PlaceholderImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    init {
        if (Preferences.read(DARK_MODE)) {
            this.setImageResource(R.drawable.ic_study_dark)
        } else {
            this.setImageResource(R.drawable.ic_study)
        }
    }

}