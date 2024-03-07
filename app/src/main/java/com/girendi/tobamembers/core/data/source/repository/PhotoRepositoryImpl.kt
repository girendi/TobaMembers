package com.girendi.tobamembers.core.data.source.repository

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.remote.api.ApiService
import com.girendi.tobamembers.core.data.source.remote.response.PhotoResponse
import com.girendi.tobamembers.core.domain.repository.PhotoRepository

class PhotoRepositoryImpl(private val apiService: ApiService): BaseRepository(), PhotoRepository {
    override suspend fun fetchListPhoto(page: Int, limit: Int): Result<List<PhotoResponse>> =
        apiRequest { apiService.fetchPhotos(page, limit) }
}