package com.hcyacg

import com.hcyacg.config.Config
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.utils.info

object Novelai : KotlinPlugin(
    JvmPluginDescription(
        id = "com.hcyacg.novelai",
        name = "novelai",
        version = "0.1.0",
    ) {
        author("Nekoer")
    }
) {

    override fun PluginComponentStorage.onLoad() {
        Config.reload()
    }

    override fun onDisable() {
        Config.save()
    }

    override fun onEnable() {
        logger.info { "Plugin loaded" }

        globalEventChannel().subscribeGroupMessages  {
            content { message.contentToString().contains("/ai text ") } quoteReply {
                Generate.textRegister(message.contentToString().replace("/ai text ",""),event = this, translated = Config.translated)
            }
            content { message.contentToString().contains("/ai image ") } quoteReply {
                Generate.imageRegister(event = this, translated = Config.translated)
            }

        }
    }
}