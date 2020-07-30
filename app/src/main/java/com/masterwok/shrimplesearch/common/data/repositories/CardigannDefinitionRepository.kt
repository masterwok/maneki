package com.masterwok.shrimplesearch.common.data.repositories

import android.content.Context
import android.content.res.AssetManager
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class CardigannDefinitionRepository @Inject constructor(
    private val appContext: Context
) : ICardigannDefinitionRepository
    , CoroutineScope by CoroutineScope(Dispatchers.IO + SupervisorJob()) {

    private val assetManager: AssetManager by lazy {
        appContext.assets
    }

    override fun getDefinitions(): List<String> = runBlocking {
        return@runBlocking readDefinitionPaths().map(
            this@CardigannDefinitionRepository::synchronousReadDefinitions
        )
    }

    // Why synchronous: https://www.remlab.net/op/nonblock.shtml
    private fun synchronousReadDefinitions(
        path: String
    ): String = BufferedReader(InputStreamReader(assetManager.open(path))).let {
        val text = it.readText()
        it.close()
        text
    }

    override fun getIndexerCount(): Int = readDefinitionPaths().count()

    private fun readDefinitionPaths(): List<String> {
        val assets = assetManager
            .list("")
            ?: emptyArray()

        return assets.filter { it.endsWith("yml") }
    }

}