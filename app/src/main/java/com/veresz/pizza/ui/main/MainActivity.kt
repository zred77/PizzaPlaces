package com.veresz.pizza.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.veresz.pizza.R
import com.veresz.pizza.ui.map.MapFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags

        supportFragmentManager.commit {
            val fragment = MapFragment.newInstance()
            replace(R.id.nav_host_fragment, fragment)
        }
    }
}
