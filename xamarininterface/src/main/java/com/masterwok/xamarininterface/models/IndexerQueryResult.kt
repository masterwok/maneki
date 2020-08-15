package com.masterwok.xamarininterface.models

import com.masterwok.xamarininterface.enums.IndexerQueryState

data class IndexerQueryResult(
    val indexer: Indexer,
    val items: List<QueryResultItem>,
    val queryState: IndexerQueryState,
    val failureReason: String?,
    val magnetCount: Int,
    val linkCount: Int
)