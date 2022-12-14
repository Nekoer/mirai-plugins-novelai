package com.hcyacg

import com.hcyacg.config.Config
import com.hcyacg.config.EhTagTranslationConfig
import com.hcyacg.data.LocalCookieJar
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
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import net.mamoe.mirai.utils.MiraiLogger
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


public object Generate {
    private var json: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = false
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
    }
    private val logger = MiraiLogger.Factory.create(this::class)
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .cookieJar(LocalCookieJar())
        .followRedirects(false)
        .followSslRedirects(false)
        .build()
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private var cookie = ""
    public fun loadCookie() {
        if (Config.username.isNotBlank() && Config.password.isNotBlank()){
            val body = FormBody.Builder()
                .add("username",Config.username)
                .add("password",Config.password).build()

            val request = Request.Builder().apply {
                url("${Config.stableDiffusionWebui}/login")
                post(body)
                addHeader("content-type","application/x-www-form-urlencoded")
            }.build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    logger.warning(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val setCookie = response.headers("set-cookie")
                    setCookie.onEach {
                        if (it.contains("access-token")){
                            cookie = it.substring(0,it.indexOf(";"))
                            return@onEach
                        }else{
                            throw RuntimeException("cookie????????????,?????????????????????????????????")
                        }
                    }
                }
            })
        }else{
            logger.warning("??????????????????????????????webui????????????,??????????????????????????????????????????")
        }
    }


    public suspend fun textRegister(tags: String, translated: Boolean = false,event : GroupMessageEvent): MessageChain {
        var seed = "";
        if (Config.seed == -1L){
            for (i in 0 until 10){
                seed = seed.plus((0..9).random())
            }
        }else{
            seed = Config.seed.toString()
        }


        val randomString = (1..11)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");



        val data = PostData(data = arrayOf(
            if(!translated) tags else tags.split(",").joinToString(",") {translate(it)},
            Config.negativePrompt,
            Config.promptStyle,//None
            Config.promptStyle2,//None
            Config.steps,//20
            Config.samplerIndex,//Euler a
            Config.restoreFaces,//false
            Config.tiling,//false
            Config.nIter,//1
            Config.batchSize,//1
            Config.cfgScale,//7
            seed.toBigInteger(),//-1
            Config.subSeed,//-1
            Config.subSeedStrength,//0
            Config.seedResizeFromH,//0
            Config.seedResizeFromW,//0
            Config.seedEnableExtras,//false
            Config.height,//512
            Config.width,//512
            Config.enableHr,//false
//            Config.scaleLatent,
            Config.denoisingStrength,//0.7
            0,
            0,
            Config.script,//None
            Config.putVariablePartsAtStartOfPrompt,//false
            false,
            false,
            "",
            Config.xtype,//seed
            Config.xvalues,
            Config.ytype,//nonthing
            Config.yvalues,
            Config.drawLegend,//true
            Config.keepRandomSeeds,//false
            false,
            null,
            "",
            ""),fn_index=Config.textFnIndex,session_hash = randomString)


        var request = Request.Builder().apply {
            url("${Config.stableDiffusionWebui}/run/predict/")
            post(json.encodeToString(PostData.serializer(),data).toRequestBody())
            addHeader("content-type","application/json")
            if (cookie.isNotBlank()) addHeader("cookie",cookie)
        }.build()
        event.subject.sendMessage("?????????,????????????1??????")
        var response = client.newCall(request).execute()
        val element = json.parseToJsonElement(response.body.string())


        val name = element.jsonObject["data"]?.jsonArray?.get(0)?.jsonArray?.get(0)?.jsonObject?.get("name")?.jsonPrimitive?.content
        request = Request.Builder().apply {
            url("${Config.stableDiffusionWebui}/file=$name")
        }.build()
        response = client.newCall(request).execute()
        val byte = response.body.bytes()

        if (null != byte){
            val toExternalResource = byte.toExternalResource()
            val image = toExternalResource.uploadAsImage(event.group)
            withContext(Dispatchers.IO) {
                toExternalResource.close()
            }

            return image.plus("\n").plus("seed: $seed")
        }
        return PlainText("????????????").toMessageChain()
    }

    public suspend fun imageRegister(translated: Boolean = false, event : GroupMessageEvent): MessageChain {
        val text = event.message.find { it is PlainText } as PlainText
        val tags = text.contentToString().replace("/ai image ","")
        val tmpImage = event.message.find { it is Image } as Image? ?: return PlainText("???????????????").toMessageChain()
        val url = tmpImage.queryUrl()
        val urlRequest = Request.Builder().apply {
            url(url)
            get()
            addHeader("content-type","application/json")
        }.build()
        val resp = client.newCall(urlRequest).execute()
        val byteArray = resp.body.bytes()
        val base64Image = "data:image/png;base64,"+Base64.getEncoder().encodeToString(byteArray)


        var seed = "";
        if (Config.seed == -1L){
            for (i in 0 until 10){
                seed = seed.plus((0..9).random())
            }
        }else{
            seed = Config.seed.toString()
        }


        val randomString = (1..11)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        val data = PostData(data = arrayOf(
            Config.mode,//0
            if(!translated) tags else tags.split(",").joinToString(",") { translate(it) },
            Config.negativePrompt,
            Config.promptStyle,//None
            Config.promptStyle2,//None
            base64Image,//????????????
            if(Config.initImgWithMask == -1) null else Config.initImgWithMask,//null
            if(Config.initImgInPaint == -1) null else Config.initImgWithMask,//null
            if(Config.initMaskInPaint == -1) null else Config.initImgWithMask,//null
            Config.maskMode,//Draw mask
            Config.steps,//20
            Config.samplerIndex,//Euler a
            Config.maskBlur,//4
            Config.inPaintingFill,//original
            Config.restoreFaces,//false
            Config.tiling,//false
            Config.nIter,//1
            Config.batchSize,//1
            Config.cfgScale,//7
            Config.denoisingStrength,//0.75
            seed.toBigInteger(),//-1
            Config.subSeed,//-1
            Config.subSeedStrength,//0
            Config.seedResizeFromH,//0
            Config.seedResizeFromW,//0
            Config.seedEnableExtras,//false
            Config.height,//512
            Config.width,//512
            Config.resizeMode,//just resize
            Config.inPaintFullRes,//false
            Config.inPaintFullResPadding,//32
            Config.inPaintingMaskInvert,//Inpaint masked
            "",
            "",
            "None",//34
            "",
            true,
            true,
            "",
            "",
            true,
            50,
            true,
            1,
            0,
            false,
            4,
            1,
            "",
            128,
            8,
            arrayOf("left","right","up", "down"),
            1,
            0.05,
            128,
            4,
            "fill",
            arrayOf("left","right","up", "down"),
            false,
            false,
            false,
            "",
            "",
            64,
            "None",
            Config.xtype,//seed
            Config.xvalues,
            Config.ytype,//nothing
            Config.yvalues,
            Config.drawLegend,//true
            Config.keepRandomSeeds,//false
            false,
            null,
            "",""),fn_index=Config.imageFnIndex,session_hash = randomString)

        var request = Request.Builder().apply {
            url("${Config.stableDiffusionWebui}/run/predict/")
            post(json.encodeToString(PostData.serializer(),data).toRequestBody())
            addHeader("content-type","application/json")
            if (cookie.isNotBlank()) addHeader("cookie",cookie)
        }.build()
        event.subject.sendMessage("?????????,????????????1??????")
        var response = client.newCall(request).execute()
        val element = json.parseToJsonElement(response.body.string())
        val name = element.jsonObject["data"]?.jsonArray?.get(0)?.jsonArray?.get(0)?.jsonObject?.get("name")?.jsonPrimitive?.content
        request = Request.Builder().apply {
            url("${Config.stableDiffusionWebui}/file=$name")
        }.build()
        response = client.newCall(request).execute()
        val byte = response.body.bytes()

        if (null != byte){
            val toExternalResource = byte.toExternalResource()
            val image = toExternalResource.uploadAsImage(event.group)
            withContext(Dispatchers.IO) {
                toExternalResource.close()
            }

            return image.plus("\n").plus("seed: $seed")
        }
        return PlainText("????????????").toMessageChain()
    }

    private fun translate(word: String): String {
        for (translation in EhTagTranslationConfig.database.data) {
            for ((name, tag) in translation.data) {
                if (tag.name == word) return name
            }
        }
        for (translation in EhTagTranslationConfig.database.data) {
            for ((name, tag) in translation.data) {
                if (tag.name.startsWith(word)) return name
            }
        }
        return translateChinese(word)
    }

    private fun translateChinese(text: String): String {
        return try{
            val request = Request.Builder().apply {
                url("https://fanyi.youdao.com/translate?&doctype=json&type=ZH_CN2EN&i=${text}")
                get()
            }.build()

            val result = json.decodeFromString<TranslateResult>(client.newCall(request).execute().body!!.string())
            result.translateResult[0][0].tgt
        }catch(e:Exception){
            logger.error("????????????????????????")
            ""
        }
    }

    // ??????Unicode??????????????????????????????????????????
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
    }

    // ????????????????????????????????????
    public fun isChinese(strName: String): Boolean {
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (isChinese(c)) {
                return true
            }
        }
        return false
    }

    // ??????????????????CJK?????????CJK???????????????
    public fun isChineseByREG(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val pattern: Pattern = Pattern.compile("[\\u4E00-\\u9FBF]+")
        return pattern.matcher(str.trim { it <= ' ' }).find()
    }

    // ??????????????????CJK?????????CJK???????????????
    public fun isChineseByName(str: String?): Boolean {
        if (str == null) {
            return false
        }
        // ??????????????????\\p ???????????????\\P ???????????????
        // \\p{Cn} ???????????? Unicode ?????????????????????????????????\\P{Cn} ????????? Unicode?????????????????????????????????
        val reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}"
        val pattern: Pattern = Pattern.compile(reg)
        return pattern.matcher(str.trim { it <= ' ' }).find()
    }
}