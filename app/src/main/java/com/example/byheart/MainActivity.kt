package com.example.byheart

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.byheart.card.CardFragment
import com.example.byheart.overview.Overview
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileFragment
import com.example.byheart.pile.PileViewModel
import com.example.byheart.shared.PileAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Arrays.asList

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    var pileId: String = ""
    var pileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        populateMenu()
    }

    private fun populateMenu() {
        val layoutManager = LinearLayoutManager(this)
        val pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        pileViewModel.allPiles.observe(this, Observer<List<Pile>> { piles ->
            val overviews = asList(Overview("Overview", piles))
            val adapter = PileAdapter(overviews)
            mainMenuRecycler.layoutManager = layoutManager
            mainMenuRecycler.adapter = adapter
        })
    }

    fun closeDrawer(): Unit = drawer_layout.closeDrawer(GravityCompat.START)

    fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    override fun onBackPressed() = when {
        drawer_layout.isDrawerOpen(GravityCompat.START) -> closeDrawer()
        else -> super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container, CardFragment()).commit()
            }
            R.id.nav_gallery -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container, PileFragment()).commit()
            }
        }

        closeDrawer()
        return true
    }
}
