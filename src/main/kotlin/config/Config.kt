package com.hcyacg.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    @ValueName("prompt")
    @ValueDescription("词条")
    var prompt: String by value()

    @ValueName("negativePrompt")
    @ValueDescription("过滤词条")
    var negativePrompt: String by value("lowres,bad anatomy,bad hands,text,error,missing fingers,extra digit,fewer digits,cropped,worst quality,low quality,normal quality,jpeg artifacts,signature,watermark,username,blurry,bad feet")

    @ValueName("promptStyle")
    @ValueDescription("词条风格")
    var promptStyle: String by value("None")

    @ValueName("promptStyle2")
    @ValueDescription("词条风格2")
    var promptStyle2: String by value("None")

    @ValueName("steps")
    @ValueDescription("步骤")
    var steps: Int by value(40)

    @ValueName("samplerIndex")
    @ValueDescription("采样器索引,Euler a|Euler|LMS|Heun|DPM2|DPM2 a|DPM fast|DPM adaptive|LMS Karras|DPM2 Karras|DPM2 a Karras|DDIM|PLMS")
    var samplerIndex: String by value("Euler a")

    @ValueName("restoreFaces")
    @ValueDescription("恢复面")
    var restoreFaces: Boolean by value(false)

    @ValueName("tiling")
    @ValueDescription("生成一个可以平铺的图像")
    var tiling: Boolean by value(false)

    @ValueName("nIter")
    @ValueDescription("尝试运行多少次")
    var nIter: Int by value(1)

    @ValueName("batchSize")
    @ValueDescription("在单个批处理中创建多少图像")
    var batchSize: Int by value(1)

    @ValueName("cfgScale")
    @ValueDescription("分类器自由引导尺度——图像与提示符的一致程度——越低的值产生越有创意的结果")
    var cfgScale: Float by value(7f)

    @ValueName("seed")
    @ValueDescription("随机种子，-1为随机 只限数字")
    var seed: Int by value(-1)

    @ValueName("subSeed")
    @ValueDescription("候补种子 只限数字")
    var subSeed: Int by value(-1)

    @ValueName("subSeedStrength")
    @ValueDescription("候补种子强度")
    var subSeedStrength: Float by value(0f)

    @ValueName("seedResizeFromH")
    @ValueDescription("种子从高调大小")
    var seedResizeFromH: Int by value(0)

    @ValueName("seedResizeFromW")
    @ValueDescription("种子从宽调大小")
    var seedResizeFromW: Int by value(0)

    @ValueName("seedEnableExtras")
    @ValueDescription("种子启用额外部分")
    var seedEnableExtras: Boolean by value(false)

    @ValueName("height")
    @ValueDescription("高度")
    var height: Int by value(512)

    @ValueName("width")
    @ValueDescription("高度")
    var width: Int by value(512)

    @ValueName("enableHr")
    @ValueDescription("未知")
    var enableHr: Boolean by value(true)

    @ValueName("scaleLatent")
    @ValueDescription("潜在的尺度")
    var scaleLatent: Boolean by value(false)

    @ValueName("denoisingStrength")
    @ValueDescription("与原图的差异度")
    var denoisingStrength: Float by value(0.7f)

    @ValueName("script")
    @ValueDescription("脚本,Prompt matrix|Prompts from file or textbox|X/Y plot")
    var script:String by value("None")

    @ValueName("xvalues")
    @ValueDescription("只有脚本选择X/Y plot后使用，否则默认为\"\"")
    var xvalues:String by value("")

    @ValueName("yvalues")
    @ValueDescription("只有脚本选择X/Y plot后使用，否则默认为\"\"")
    var yvalues:String by value("")

    @ValueName("drawLegend")
    @ValueDescription("画的传说？搞不懂")
    var drawLegend: Boolean by value(true)

    @ValueName("keepRandomSeeds")
    @ValueDescription("保持随机")
    var keepRandomSeeds : Boolean by value(false)

    @ValueName("putVariablePartsAtStartOfPrompt")
    @ValueDescription("在词条的开始处放置可变部件")
    var putVariablePartsAtStartOfPrompt: Boolean by value(false)
}