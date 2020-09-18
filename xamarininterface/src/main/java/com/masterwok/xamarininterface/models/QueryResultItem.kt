package com.masterwok.xamarininterface.models

import android.net.Uri
import java.util.*

data class QueryResultItem(
    val title: String,
    val description: String?,
    val linkInfo: LinkInfo,
    val socialInfo: SocialInfo,
    val statInfo: StatInfo
) {
    data class LinkInfo(
        val magnetUri: Uri?,
        val infoHash: String?,
        val link: Uri?,
        val comments: Uri?,
        val bannerUri: Uri?
    )

    data class SocialInfo(
        val rageId: Long?,
        val tvdbId: Long?,
        val imdb: Long?,
        val tmdb: Long?
    )

    data class StatInfo(
        val publishedOn: Date?,
        val seeders: Long?,
        val peers: Long?,
        val size: Long?,
        val files: Long?,
        val grabs: Long?,
        val minimumRatio: Double?,
        val minimumSeedTime: Long?,
        val downloadVolumeFactor: Double?,
        val uploadVolumeFactor: Double?
    )
}




