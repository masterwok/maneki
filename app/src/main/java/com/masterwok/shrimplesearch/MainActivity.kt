package com.masterwok.shrimplesearch

import android.app.Activity
import android.os.Bundle
import com.masterwok.jackett.JackettHarness
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val jackettHarness = JackettHarness(object : ICardigannDefinitionRepository {

            override fun getDefinitions(): List<String> {
                return emptyList()
            }

            override fun getIndexerCount(): Int {
                return 0
            }
        })

        var isInitialized = jackettHarness.initialize()
    }
}
