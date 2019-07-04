package com.example.byheart.rehearsal.views

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class RehearsalCardsLayout : RelativeLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}