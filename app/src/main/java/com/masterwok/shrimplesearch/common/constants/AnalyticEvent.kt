package com.masterwok.shrimplesearch.common.constants

enum class AnalyticEvent(val eventName: String) {
    Search("search"),
    MenuItemSortTapped("menu_item_sort_tapped"),
    MenuItemAboutTapped("menu_item_about_tapped"),
    OpenResult("result_open"),
    CopyResult("result_copy"),
    ShareResult("result_share"),
    ShareManeki("share_maneki")
}