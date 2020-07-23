package com.masterwok.shrimplesearch

import android.app.Activity
import android.os.Bundle
import com.masterwok.jackett.JackettHarness
import com.masterwok.xamarin.JackettHarnessFactory
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarness

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val jackettHarness =
            JackettHarnessFactory.createInstance(object : ICardigannDefinitionRepository {
                override fun getDefinitions(): List<String> {
                    return emptyList()
                }

                override fun getIndexerCount(): Int {
                    return 0
                }

            })

        var foo = jackettHarness.isInitialized

        jackettHarness.initialize()

        var bar = jackettHarness.isInitialized
    }
}
