package com.masterwok.shrimplesearch.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.masterwok.shrimplesearch.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar_maneki.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initSupportActionBar()
        initNavigation()
    }

    private fun initSupportActionBar() {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentNavHost) as NavHostFragment

        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navigationView.setupWithNavController(navController)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
