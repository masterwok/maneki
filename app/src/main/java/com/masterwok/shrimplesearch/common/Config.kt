package com.masterwok.shrimplesearch.common

import com.masterwok.shrimplesearch.common.constants.Theme
import com.masterwok.shrimplesearch.common.data.models.UserSettings

/**
 * The name of the application shared preferences.
 */
const val SHARED_PREFERENCES_NAME = "maenki.shared_preferences"

/**
 * The unique identifier for the aggregate indexer.
 */
const val AGGREGATE_INDEXER_ID = "aggregate_indexer"

/**
 * The indexer block list defines the unique identifiers of indexers blocked to comply with Google
 * store policy.
 */
val INDEXER_BLOCK_LIST = listOf(
    "empornium",
    "empornium2fa",
    "gay-torrents",
    "gay-torrentsorg",
    "gaytorrentru",
    "leporno",
    "mypornclub",
    "pornbay",
    "pornbits",
    "pornforall",
    "pornleech",
    "pornolive",
    "pornorip",
    "pornotor",
    "pussytorrents",
    "sexypics",
    "xxxtor",
    "xxxtorrents"
)


/**
 * The default user settings configuration.
 */
val DEFAULT_USER_SETTINGS = UserSettings(
    theme = Theme.Light,
    isScrollToTopNotificationsEnabled = true,
    isExistDialogEnabled = true
)

