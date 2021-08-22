package com.sergiom.gnb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergiom.gnb.R
import com.sergiom.gnb.ui.gnbfragment.GNBFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GNBFragment.newInstance())
                .commitNow()
        }
    }
}