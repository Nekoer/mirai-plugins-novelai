package com.hcyacg

import com.hcyacg.config.Config
import com.hcyacg.config.EhTagTranslationConfig
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.event.subscribeGroupTempMessages
import net.mamoe.mirai.utils.info

public object Novelai : KotlinPlugin(
    JvmPluginDescription(
        id = "com.hcyacg.novelai",
        name = "novelai",
        version = "0.7.4",
    ) {
        author("Nekoer")
    }
) {

    @OptIn(ConsoleExperimentalApi::class)
    override fun PluginComponentStorage.onLoad() {
        launch {
            EhTagTranslationConfig.onInit()
        }
        Config.reload()
    }

    override fun onDisable() {
        Config.save()
    }

    override fun onEnable() {
        logger.info { "Plugin loaded" }
        Generate.loadCookie()

        globalEventChannel().subscribeGroupTempMessages {
            content { Config.owner.find { it == subject.id.toString() }?.isNotBlank() ?: false  && message.contentToString().contains("/ai username ") } quoteReply {
                Config.username = message.contentToString().replace("/ai username ","")
                Config.save()
                "用户名已修改成功"
            }

            content { Config.owner.find { it == subject.id.toString() }?.isNotBlank() ?: false && message.contentToString().contains("/ai password ") } quoteReply {
                Config.password = message.contentToString().replace("/ai password ","")
                Config.save()
                "密码已修改成功"
            }
        }
        globalEventChannel().subscribeFriendMessages {
            content { Config.owner.find { it == subject.id.toString() }?.isNotBlank() ?: false  && message.contentToString().contains("/ai username ") } quoteReply {
                Config.username = message.contentToString().replace("/ai username ","")
                Config.save()
                "用户名已修改成功"
            }

            content { Config.owner.find { it == subject.id.toString() }?.isNotBlank() ?: false && message.contentToString().contains("/ai password ") } quoteReply {
                Config.password = message.contentToString().replace("/ai password ","")
                Config.save()
                "密码已修改成功"
            }
        }

        globalEventChannel().subscribeGroupMessages  {
            content { message.contentToString().contains("/ai text ") } quoteReply {
                Generate.textRegister(message.contentToString().replace("/ai text ",""),event = this, translated = Config.translated)
            }
            content { message.contentToString().contains("/ai image ") } quoteReply {
                Generate.imageRegister(event = this, translated = Config.translated)
            }
            content { message.contentToString().contains("/ai seed ") } quoteReply {
                try {
                    Config.seed = message.contentToString().replace("/ai seed ","").toLong()
                    Config.save()
                    "随机数种子已经设置为${Config.seed}"
                }catch (e:Exception){
                    e.printStackTrace()
                    "随机数种子已经设置为${Config.seed}"
                }
            }

            content { message.contentToString().contains("/ai 翻译") } quoteReply {
                try {
                    Config.translated = !Config.translated
                    Config.save()
                    "翻译已${if(Config.translated)"开启" else "关闭"}"
                }catch (e:Exception){
                    e.printStackTrace()
                    "翻译已${if(Config.translated)"开启" else "关闭"}"
                }
            }
            content { message.contentToString().contains("/ai url ") } quoteReply {
                try{
                    Config.stableDiffusionWebui = message.contentToString().replace("/ai url ","")
                    Config.save()
                    "源站已更改为${Config.stableDiffusionWebui}"
                }catch (e:Exception){
                    e.printStackTrace()
                    "更改错误"
                }

            }
        }
    }
}