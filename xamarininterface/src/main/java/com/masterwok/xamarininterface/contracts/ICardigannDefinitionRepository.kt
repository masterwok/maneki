package com.masterwok.xamarininterface.contracts

interface ICardigannDefinitionRepository {

    fun getDefinitions(): List<String>

    fun getIndexerCount(): Int

}
