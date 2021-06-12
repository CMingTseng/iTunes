package com.sion.itunes.model.vo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "musics")
data class Music(
        @PrimaryKey
        val trackId: Long = -1,
        val artistName: String = "",
        val trackName: String = "",
        val artworkUrl100: String = "",
        val previewUrl: String = "",
        var keyword: String = "",
        var insertIndex: Long = 0,
        val wrapperType: String? = null,
        val kind: String? = null,
        val artistId: Int = 0,
        val collectionId: Int = 0,
        val collectionName: String? = null,
        val collectionCensoredName: String? = null,
        val trackCensoredName: String? = null,
        val artistViewUrl: String? = null,
        val collectionViewUrl: String? = null,
        val trackViewUrl: String? = null,
        val artworkUrl30: String? = null,
        val artworkUrl60: String? = null,
        val collectionPrice: Double = 0.0,
        val trackPrice: Double = 0.0,
        val releaseDate: String? = null,
        val collectionExplicitness: String? = null,
        val trackExplicitness: String? = null,
        val discCount: Int = 0,
        val discNumber: Int = 0,
        val trackCount: Int = 0,
        val trackNumber: Int = 0,
        val trackTimeMillis: Int = 0,
        val country: String? = null,
        val currency: String? = null,
        val primaryGenreName: String? = null,
        val isStreamable: Boolean = false,
        val copyright: String? = null,
        val description: String? = null,
        val contentAdvisoryRating: String? = null,
        val collectionArtistId: Int = 0,
        val collectionArtistName: String? = null,
        val collectionArtistViewUrl: String? = null,
)
