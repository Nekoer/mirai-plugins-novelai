package com.hcyacg.data

import kotlinx.serialization.Serializable

@Serializable
public data class TranslateResult(
    val elapsedTime: Int,
    val errorCode: Int,
    val translateResult: List<List<TranslateResultX>>,
    val type: String
)