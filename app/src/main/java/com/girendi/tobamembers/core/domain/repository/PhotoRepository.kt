package com.girendi.tobamembers.core.domain.repository

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.remote.response.PhotoResponse

interface PhotoRepository {
    suspend fun fetchListPhoto(page: Int, limit: Int): Result<List<PhotoResponse>>
}