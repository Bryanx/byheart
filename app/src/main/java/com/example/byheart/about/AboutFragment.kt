package com.example.byheart.about

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.byheart.R
import com.example.byheart.pile.PileFragment
import com.example.byheart.shared.*
import kotlinx.android.synthetic.main.content_about.*


/**
 * In this fragment piles are editing or created.
 * @author Bryan de Ridder
 */
class AboutFragment : Fragment(), IOnBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = container!!.inflate(R.layout.content_about)
        addToolbar(hasOptions = false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = context?.color(R.color.colorPrimary)!!
        }
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rateUs.setOnClickListener {}
        buyCoffee.setOnClickListener { goToUrl( "https://bunq.me/PayBryan/3")}
    }

    override fun onBackPressed(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = context?.getAttr(R.attr.mainBackgroundColor)!!
        }
        startFragment(PileFragment())
        return true
    }

    private fun goToUrl(url: String) {
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        launchBrowser.addCategory(Intent.CATEGORY_BROWSABLE)
        startActivity(launchBrowser)
    }
}
