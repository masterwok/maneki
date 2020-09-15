package com.masterwok.shrimplesearch.common.constants

enum class AnalyticEvent(val eventName: String) {
    Query("query"),
    QueryLocal("query_local"),
    MenuItemSortTapped("menu_item_sort_tapped"),
    MenuItemAboutTapped("menu_item_about_tapped")
}