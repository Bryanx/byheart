package com.example.byheart.about

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.byheart.R
import com.example.byheart.pile.PileFragment
import com.example.byheart.shared.*


/**
 * In this fragment piles are editing or created.
 * @author Bryan de Ridder
 */
class AboutFragment : Fragment(), IOnBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = container!!.inflate(R.layout.content_about)
        addToolbar(true, "", false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = context?.color(R.color.colorPrimary)!!
        }
        return layout
    }

    override fun onBackPressed(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = context?.getAttr(R.attr.mainBackgroundColor)!!
        }
        startFragment(
            PileFragment(),
            R.animator.slide_down,
            R.animator.slide_up
        )
        return true
    }
}
