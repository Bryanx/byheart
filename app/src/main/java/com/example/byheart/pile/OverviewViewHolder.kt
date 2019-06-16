package com.example.byheart.pile

import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.example.byheart.R
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class OverviewViewHolder(itemView: View) : GroupViewHolder(itemView) {

    private val pileTitle: TextView = itemView.findViewById(R.id.pile_title)
    private val arrow: ImageView = itemView.findViewById(R.id.icon_expand)

    fun setOverviewTitle(group: ExpandableGroup<*>) {
        pileTitle.text = group.title
    }

    override fun expand(): Unit = animateExpand()

    override fun collapse(): Unit = animateCollapse()

    private fun animateExpand() {
        val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.startAnimation(rotate)
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.startAnimation(rotate)
    }
}