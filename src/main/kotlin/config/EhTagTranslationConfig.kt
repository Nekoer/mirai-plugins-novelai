package com.hcyacg.config

import com.hcyacg.data.EhTagTranslationDatabase
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.client.statement.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders.ContentEncoding
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.util.*
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File

public object  EhTagTranslationConfig{
    private var logger = MiraiLogger.Factory.create(this::class)
    public var database: EhTagTranslationDatabase = EhTagTranslationDatabase.Empty
        get() {
            return if (field.data.isEmpty() && database0.exists()) {
                val database = Json.decodeFromString(EhTagTranslationDatabase.serializer(), database0.readText())
                field = database
                database
            } else {
                field
            }
        }
        set(value) {
            database0.writeText(Json.encodeToString(EhTagTranslationDatabase.serializer(), value))
            field = value
        }



    private var database0 = File("./config/com.hcyacg.novelai/db.text.json")

    @ConsoleExperimentalApi
    public suspend fun onInit() {
        try {
            if (!database0.exists()){
                database0.createNewFile()
            }
            val http = HttpClient(OkHttp) {
                BrowserUserAgent()
                ContentEncoding
            }
            val statement: HttpResponse =
                http.get("https://github.com/EhTagTranslation/Database/releases/latest/download/db.text.json")
            while (statement.isActive) {
                try {
                    database0.writeBytes(statement.readBytes())
                    break
                } catch (_: SocketTimeoutException) {
                    continue
                } catch (_: ConnectTimeoutException) {
                    continue
                } catch (_: java.net.SocketException) {
                    logger.warning("翻译词典下载失败，正在尝试重新下载")
                    continue
                }
            }
        }catch(e:Exception) {
            e.printStackTrace()
        }
    }

}