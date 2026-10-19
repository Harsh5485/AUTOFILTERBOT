package com.harsh.streaming

import com.harsh.streaming.model.MediaType
import com.harsh.streaming.model.StreamProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogRepositoryUnitTest {

    @Test
    fun streamProvider_getEmbedUrl_movie_returnsCorrectIcuUrl() {
        val provider = StreamProvider("vidsrc_icu", "Server 1", "Fast Server")
        val url = provider.getEmbedUrl(MediaType.MOVIE, 550)
        assertEquals("https://vidsrc.icu/embed/movie/550", url)
    }

    @Test
    fun streamProvider_getEmbedUrl_tv_returnsCorrectIcuUrl() {
        val provider = StreamProvider("vidsrc_icu", "Server 1", "Fast Server")
        val url = provider.getEmbedUrl(MediaType.TV, 1399, season = 2, episode = 4)
        assertEquals("https://vidsrc.icu/embed/tv/1399/2/4", url)
    }

    @Test
    fun streamProvider_getEmbedUrl_tv_returnsCorrectVidLinkUrl() {
        val provider = StreamProvider("vidlink", "Server 5", "Backup Server")
        val url = provider.getEmbedUrl(MediaType.TV, 1399, season = 1, episode = 1)
        assertEquals("https://vidlink.pro/tv/1399/1/1", url)
    }
}
