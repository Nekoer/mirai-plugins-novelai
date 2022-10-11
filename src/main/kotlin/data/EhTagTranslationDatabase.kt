package com.hcyacg.data
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonObject

@Serializable
public data class EhTagTranslationDatabase(
    @SerialName("data")
    val `data`: List<EhTagTranslation>,
    @SerialName("head")
    val head: EhTagTranslationHead?,
    @SerialName("repo")
    val repo: String,
    @SerialName("version")
    val version: Int
) {
    public companion object {
        public val Empty: EhTagTranslationDatabase = EhTagTranslationDatabase(
            data = emptyList(),
            head = null,
            repo = "",
            version = 0
        )
    }
}

@Serializable
public data class EhTagTranslation(
    @SerialName("count")
    val count: Int,
    @SerialName("data")
    val `data`: Map<String, EhTag>,
    @SerialName("frontMatters")
    val frontMatters: EhTagFrontMatters,
    @SerialName("namespace")
    val namespace: String
)

@Serializable
public data class EhTag(
    @SerialName("intro")
    val intro: String,
    @SerialName("links")
    val links: String,
    @SerialName("name")
    val name: String
)

@Serializable
public data class EhTagAuthor(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("when")
    val `when`: String
)

@Serializable
public data class EhTagFrontMatters(
    @SerialName("abbr")
    val abbr: String = "",
    @SerialName("aliases")
    val aliases: List<String> = emptyList(),
    @SerialName("copyright")
    val copyright: String = "",
    @SerialName("description")
    val description: String,
    @SerialName("example")
    val example: JsonObject? = null,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("rules")
    val rules: List<String>
)

@Serializable
public data class EhTagTranslationHead(
    @SerialName("author")
    val author: EhTagAuthor,
    @SerialName("committer")
    val committer: EhTagAuthor,
    @SerialName("message")
    val message: String,
    @SerialName("sha")
    val sha: String
)