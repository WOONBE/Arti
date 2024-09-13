package com.hexa.arti.data.model.response

data class ErrorResponse(
    val code: Int?,
    val message: String?
)


data class ApiException(
    val code : Int?,
    override val message : String?
) : Exception(message)
