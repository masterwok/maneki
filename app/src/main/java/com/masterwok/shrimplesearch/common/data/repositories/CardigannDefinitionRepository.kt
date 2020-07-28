package com.masterwok.shrimplesearch.common.data.repositories

import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import javax.inject.Inject

class CardigannDefinitionRepository @Inject constructor(): ICardigannDefinitionRepository {

    override fun getDefinitions(): List<String> = emptyList()

    override fun getIndexerCount(): Int = 0

}