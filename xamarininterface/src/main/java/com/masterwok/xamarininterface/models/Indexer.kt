package com.masterwok.xamarininterface.models

import com.masterwok.xamarininterface.enums.IndexerType

data class Indexer(
    val id: String,
    val type: IndexerType,
    val displayName: String,
    val displayDescription: String?
)