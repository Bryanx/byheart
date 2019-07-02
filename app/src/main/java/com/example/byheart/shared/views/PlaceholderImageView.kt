package com.example.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.example.byheart.R
import com.example.byheart.shared.Preferences


class PlaceholderImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    init {
        if (Preferences.read(Preferences.DARK_MODE, false)) {
            this.setImageResource(R.drawable.ic_study_dark)
        } else {
            this.setImageResource(R.drawable.ic_study)
        }
    }

}