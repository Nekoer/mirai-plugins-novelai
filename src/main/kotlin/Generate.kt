package com.hcyacg

import com.hcyacg.config.Config
import com.hcyacg.data.PostData
import com.hcyacg.data.TranslateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import java.util.concurrent.TimeUnit

object Generate {
    var json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = false
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
    }
    private val client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).build()
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    suspend fun register(tags: String, translated: Boolean = false,event : GroupMessageEvent):Image {
        var seed = "";
        if (Config.seed == -1){
            for (i in 0 until 10){
                seed = seed.plus((0..9).random())
            }
        }else{
            seed = Config.seed.toString()
        }


        val randomString = (1..11)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        val data = PostData(data = arrayOf(tags,
            Config.negativePrompt,
            Config.promptStyle,
            Config.promptStyle2,
            Config.steps,
            Config.samplerIndex,
            Config.restoreFaces,
            Config.tiling,
            Config.nIter,
            Config.batchSize,
            Config.cfgScale,
            seed.toBigInteger(),
            Config.subSeed,
            Config.subSeedStrength,
            Config.seedResizeFromH,
            Config.seedResizeFromW,
            Config.seedEnableExtras,
            Config.height,
            Config.width,
            Config.enableHr,
            Config.scaleLatent,
            Config.denoisingStrength,
            Config.script,
            Config.putVariablePartsAtStartOfPrompt,
            false,
            null,
            "",
            "Seed",
            Config.xvalues,
            "Steps",
            Config.yvalues,
            Config.drawLegend,
            Config.keepRandomSeeds,
            null,
            "",""),fn_index=11,session_hash = randomString)
        val request = Request.Builder().apply {
            url("http://127.0.0.1:7860/api/predict/")
            post(json.encodeToString(PostData.serializer(),data).toRequestBody())
//            post(json.encodeToString(data).toRequestBody())
            addHeader("content-type","application/json")

        }.build()
        event.subject.sendMessage("请稍后,预计需要1分钟")
        val response = client.newCall(request).execute()
        val element = json.parseToJsonElement(response.body!!.string())
        var base64 = element.jsonObject["data"]?.jsonArray?.get(0)?.jsonArray?.get(0)?.jsonPrimitive?.content

        if (base64?.contains("data:") == true) {
            val start: Int = base64.indexOf(",")
            base64 = base64.substring(start + 1)
        }
        var file = base64?.replace("\r|\n", "");
        file = file?.trim();

        val toExternalResource = Base64.getDecoder().decode(file).toExternalResource()
        val image = toExternalResource.uploadAsImage(event.group)
        withContext(Dispatchers.IO) {
            toExternalResource.close()
        }

        return image
    }

    fun translate(text: String): String {
        val request = Request.Builder().apply {
            url("https://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=${text}")
            get()
        }.build()

        val result = json.decodeFromString<TranslateResult>(client.newCall(request).execute().body!!.string())
        return result.translateResult[0][0].tgt
    }
}