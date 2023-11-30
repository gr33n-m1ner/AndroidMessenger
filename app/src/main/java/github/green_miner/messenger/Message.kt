package github.green_miner.messenger

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    @field:Json(name = "from") val from: String,
    @field:Json(name = "to") val to: String,
    @field:Json(name = "time") val time: Long,
    @field:Json(name = "data") val data: MessageData,
    @field:Json(name = "id") val id: Int
)

@JsonClass(generateAdapter = true)
data class MessageData(
    @field:Json(name = "Text") val Text: Text?,
    @field:Json(name = "Image") val Image: Image?
)

@JsonClass(generateAdapter = true)
data class Text(@field:Json(name = "text") val text: String)

@JsonClass(generateAdapter = true)
data class Image(@field:Json(name = "link") val link: String)