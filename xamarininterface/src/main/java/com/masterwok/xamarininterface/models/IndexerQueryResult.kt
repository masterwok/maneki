package com.masterwok.xamarininterface.models

import com.masterwok.xamarininterface.enums.QueryState

data class IndexerQueryResult(
    val indexer: Indexer,
    val items: List<QueryResultItem>,
    val queryState: QueryState,
    val failureReason: String?
)