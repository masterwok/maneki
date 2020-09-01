package com.masterwok.shrimplesearch.common.constants

enum class AnalyticEvent(val eventName: String) {
    Search("search"),
    NoTorrentAppFound("no_torrent_app_found"),
    MenuItemSortTapped("menu_item_sort_tapped"),
    MenuItemAboutTapped("menu_item_about_tapped"),
    NoActionViewActivity("no_action_view_activity"),
}