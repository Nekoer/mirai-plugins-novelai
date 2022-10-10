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
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Character.UnicodeBlock
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

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
    suspend fun textRegister(tags: String, translated: Boolean = false,event : GroupMessageEvent):Image {
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

        val data = PostData(data = arrayOf(if(!translated && !isChinese(tags)) tags else tags.split(",").joinToString(",") { translate(it) },
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

    suspend fun imageRegister(translated: Boolean = false, event : GroupMessageEvent):Image{
        val text = event.message.find { it is PlainText } as PlainText
        val tags = text.contentToString().replace("/ai image ","")
        val tmpImage = event.message.find { it is Image } as Image
        val url = tmpImage.queryUrl()
        val urlRequest = Request.Builder().apply {
            url(url)
            get()
            addHeader("content-type","application/json")
        }.build()
        val resp = client.newCall(urlRequest).execute()
        val byteArray = resp.body?.bytes()
        val base64Image = "data:image/png;base64,"+Base64.getEncoder().encodeToString(byteArray)


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

        val data = PostData(data = arrayOf(
            Config.mode,
            if(!translated && !isChinese(tags)) tags else tags.split(",").joinToString(",") { translate(it) },
            Config.negativePrompt,
            Config.promptStyle,
            Config.promptStyle2,
            base64Image,//原始图片
            if(Config.initImgWithMask == -1) null else Config.initImgWithMask,
            if(Config.initImgInPaint == -1) null else Config.initImgWithMask,
            if(Config.initMaskInPaint == -1) null else Config.initImgWithMask,
            Config.maskMode,
            Config.steps,
            Config.samplerIndex,
            Config.maskBlur,
            Config.inPaintingFill,
            Config.restoreFaces,
            Config.tiling,
            Config.nIter,
            Config.batchSize,
            Config.cfgScale,
            Config.denoisingStrength,
            seed.toBigInteger(),
            Config.subSeed,
            Config.subSeedStrength,
            Config.seedResizeFromH,
            Config.seedResizeFromW,
            Config.seedEnableExtras,
            Config.height,
            Config.width,
            Config.resizeMode,
            Config.inPaintFullRes,
            Config.inPaintFullResPadding,
            Config.inPaintingMaskInvert,
            "",
            "",
            "None",
            "",
            "",
            1,50,0,false,4,1,"",128,8,
            arrayOf("left","right","up", "down"),
            1,0.05,128,4,"fill",
            arrayOf("left","right","up", "down"),
            false,false,null,"","",64,
            "None",
            "Seed",
            Config.xvalues,
            "Steps",
            Config.yvalues,
            Config.drawLegend,
            Config.keepRandomSeeds,
            null,
            "",""),fn_index=29,session_hash = randomString)

        val request = Request.Builder().apply {
            url("http://127.0.0.1:7860/api/predict/")
            post(json.encodeToString(PostData.serializer(),data).toRequestBody())
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

    private fun translate(text: String): String {
        val request = Request.Builder().apply {
            url("https://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=${text}")
            get()
        }.build()

        val result = json.decodeFromString<TranslateResult>(client.newCall(request).execute().body!!.string())
        return result.translateResult[0][0].tgt
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private fun isChinese(c: Char): Boolean {
        val ub = UnicodeBlock.of(c)
        return ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == UnicodeBlock.GENERAL_PUNCTUATION
    }

    // 完整的判断中文汉字和符号
    fun isChinese(strName: String): Boolean {
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (isChinese(c)) {
                return true
            }
        }
        return false
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    fun isChineseByREG(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val pattern: Pattern = Pattern.compile("[\\u4E00-\\u9FBF]+")
        return pattern.matcher(str.trim { it <= ' ' }).find()
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    fun isChineseByName(str: String?): Boolean {
        if (str == null) {
            return false
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        val reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}"
        val pattern: Pattern = Pattern.compile(reg)
        return pattern.matcher(str.trim { it <= ' ' }).find()
    }
}