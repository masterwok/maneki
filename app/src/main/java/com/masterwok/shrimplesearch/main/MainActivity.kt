package com.masterwok.shrimplesearch.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.masterwok.shrimplesearch.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
