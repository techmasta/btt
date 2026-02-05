package com.bobteachestech.app.data

import okhttp3.OkHttpClient
import okhttp3.Request

class FeedRepository(
    private val client: OkHttpClient = OkHttpClient()
) {
    suspend fun fetchPosts(): List<FeedItem> {
        val xml = fetchUrl(POSTS_FEED_URL)
        return FeedParser.parseRss(xml)
    }

    suspend fun fetchVideos(): List<FeedItem> {
        val xml = fetchUrl(YOUTUBE_FEED_URL)
        return FeedParser.parseAtom(xml)
    }

    private fun fetchUrl(url: String): String {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("Unexpected response ${response.code} from $url")
            }
            return response.body?.string().orEmpty()
        }
    }

    companion object {
        const val POSTS_FEED_URL = "https://bobteachestech.com/feed/"
        const val YOUTUBE_FEED_URL = "https://www.youtube.com/feeds/videos.xml?user=bobteachestech"
    }
}
