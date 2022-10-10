package com.hcyacg.data

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResult(
    val elapsedTime: Int,
    val errorCode: Int,
    val translateResult: List<List<TranslateResultX>>,
    val type: String
)