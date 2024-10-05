package com.hexa.arti.repository

import com.hexa.arti.data.model.response.PostSubscribeResponse

interface MemberRepository {

    suspend fun postSubscribe(memberId: Int, galleryId: Int): Result<PostSubscribeResponse>

    suspend fun postUnsubscribe(memberId: Int, galleryId: Int): Result<PostSubscribeResponse>
}