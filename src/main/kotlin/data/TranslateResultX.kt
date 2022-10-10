package com.hcyacg.data

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResultX(
    val src: String,
    val tgt: String
)