package com.hcyacg.data

import kotlinx.serialization.Serializable

@Serializable
public data class TranslateResultX(
    val src: String,
    val tgt: String
)