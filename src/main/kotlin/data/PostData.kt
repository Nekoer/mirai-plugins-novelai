package com.hcyacg.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

private fun Any?.toJsonElement(): JsonElement{
    return when (this) {
        null -> JsonNull
        is JsonElement -> this
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Iterable<*> -> JsonArray(this.map { it.toJsonElement() })
        // !!! key simply converted to string
        is Map<*, *> -> JsonObject(this.map { it.key.toString() to it.value.toJsonElement() }.toMap())
        // add custom convert
        is Array<*> -> JsonArray(this.map { it.toJsonElement() })
        else -> throw Exception("不支持类型 ${this::class}=${this}}")
    }
}

private fun JsonPrimitive.toAnyValue():Any?{
    val content = this.content
    if (this.isString){
        // add custom string convert
        return content
    }
    if (content.equals("null", ignoreCase = true)){
        return null
    }
    if (content.equals("true", ignoreCase = true)){
        return true
    }
    if (content.equals("false", ignoreCase = true)){
        return false
    }
    val intValue = content.toIntOrNull()
    if (intValue!=null){
        return intValue
    }
    val longValue = content.toLongOrNull()
    if (longValue!=null){
        return longValue
    }
    val doubleValue = content.toDoubleOrNull()
    if (doubleValue!=null){
        return doubleValue
    }
    throw Exception("未知值：${content}")
}

private fun JsonElement.toAnyOrNull():Any?{
    return when (this) {
        is JsonNull -> null
        is JsonPrimitive -> toAnyValue()
        // !!! key convert back custom object
        is JsonObject -> this.map { it.key to it.value.toAnyOrNull() }.toMap()
        is JsonArray -> this.map { it.toAnyOrNull() }
    }
}

public object AnyValueSerializer  : KSerializer<Any?> {
    private val delegateSerializer = JsonElement.serializer()
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor
    override fun serialize(encoder: Encoder, value: Any?) {
        encoder.encodeSerializableValue(delegateSerializer, value.toJsonElement())
    }
    override fun deserialize(decoder: Decoder): Any? {
        val jsonPrimitive = decoder.decodeSerializableValue(delegateSerializer)
        return jsonPrimitive.toAnyOrNull()
    }
}
@Serializable
public data class PostData(
    val data: Array<@Serializable(with = AnyValueSerializer::class) Any?> = arrayOf(),
    val fn_index: Int = 11,
    val session_hash: String ="mqmlyym41fl"
)