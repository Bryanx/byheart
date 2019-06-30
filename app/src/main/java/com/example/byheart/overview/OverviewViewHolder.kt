package com.example.byheart.overview

import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.pile.edit.PileEditFragment
import com.example.byheart.shared.startFragment
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class OverviewViewHolder(itemView: View) : GroupViewHolder(itemView) {

    private val pileTitle: TextView = itemView.findViewById(R.id.pile_title)
    private val arrow: ImageView = itemView.findViewById(R.id.icon_expand)
    private val btnAddPile: ImageButton = itemView.findViewById(R.id.list_group_button_add_pile)

    init {
        btnAddPile.setOnClickListener {
            val activity = (itemView.context as MainActivity)
            activity.pileId = ""
            activity.supportFragmentManager.startFragment(PileEditFragment())
        }
    }

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