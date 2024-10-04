package com.hexa.arti.repository

import com.hexa.arti.data.model.artworkupload.ArtWorkAIUploadDto
import com.hexa.arti.data.model.artworkupload.ArtWorkUploadDto
import com.hexa.arti.data.model.survey.SurveyListDto
import com.hexa.arti.data.model.survey.SurveyResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface ArtWorkUploadRepository {
    suspend fun postMakeAIImage(
        contentImage: MultipartBody.Part,
        styleImage: RequestBody
    ): Result<ArtWorkUploadDto>

    suspend fun getImage(
        imagePath : String
    ): Result<ResponseBody>

    suspend fun getSurveyImage() : Result<SurveyResponse>

    suspend fun saveSurvey(
        surveyListDto: SurveyListDto
    ) : Result<ResponseBody>

}