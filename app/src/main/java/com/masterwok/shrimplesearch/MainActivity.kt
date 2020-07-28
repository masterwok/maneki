package com.masterwok.shrimplesearch

import android.app.Activity
import android.os.Bundle
import com.masterwok.xamarin.JackettHarnessFactory
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarnessCallbacks

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val jackettHarness = JackettHarnessFactory.createInstance(
            CardigannDefinitionRepository()
            , JackettHarnessCallbacks()
        )

        jackettHarness.initialize()
    }
}

private class JackettHarnessCallbacks : IJackettHarnessCallbacks {
    override fun onIndexersInitialized() {
    }

    override fun OnIndexerInitialized() {
    }
}

private class CardigannDefinitionRepository : ICardigannDefinitionRepository {
    override fun getDefinitions(): List<String> {
        return emptyList()
    }

    override fun getIndexerCount(): Int {
        return 0
    }
}
