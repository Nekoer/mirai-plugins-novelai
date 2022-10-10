package com.hcyacg.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    @ValueName("mode")
    @ValueDescription("模式，仅限image2image")
    var mode:Int by value(0)

    @ValueName("initImgWithMask")
    @ValueDescription("[暂时不清楚该填什么]原始图蒙版模式，仅限image2image")
    var initImgWithMask:Int by value(-1)

    @ValueName("initImgInPaint")
    @ValueDescription("[暂时不清楚该填什么]原始图图层，仅限image2image")
    var initImgInPaint:Int by value(-1)

    @ValueName("initMaskInPaint")
    @ValueDescription("[暂时不清楚该填什么]原始蒙版图层，仅限image2image")
    var initMaskInPaint:Int by value(-1)

    @ValueName("maskMode")
    @ValueDescription("蒙版模式，仅限image2image,[Upload mask|Draw mask]")
    var maskMode:String by value("Draw mask")

    @ValueName("maskBlur")
    @ValueDescription("模糊遮罩滤镜，仅限image2image")
    var maskBlur:Int by value(4)

    @ValueName("inPaintingFill")
    @ValueDescription("修复填充，仅限image2image，[fill|original|latent noise|latent nothing]")
    var inPaintingFill:String by value("original")

    @ValueName("resizeMode")
    @ValueDescription("尺寸调整模式，仅限image2image [Crop and resize|Just resize|Resize and fill]")
    var resizeMode:String by value("Just resize")

    @ValueName("inPaintFullRes")
    @ValueDescription("尺寸调整模式，仅限image2image")
    var inPaintFullRes:Boolean by value(false)

    @ValueName("inPaintFullResPadding")
    @ValueDescription("图层全分辨率填充，仅限image2image")
    var inPaintFullResPadding:Int by value(32)

    @ValueName("inPaintingMaskInvert")
    @ValueDescription("图层模板倒置，仅限image2image,[Inpaint not masked|Inpaint masked]")
    var inPaintingMaskInvert:String by value("Inpaint masked")

    @ValueName("translated")
    @ValueDescription("翻译词条")
    var translated: Boolean by value(true)

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
    @ValueDescription("采样器索引,Euler a|Euler|LMS|Heun|DPM2|DPM2 a|DPM fast|DPM adaptive|LMS Karras|DPM2 Karras|DPM2 a Karras|DDIM|PLMS, 图片转图片[Euler a|Euler|LMS|Heun|DPM2|DPM2 a|LMS Karras|DPM2 Karras|DPM2 a Karras|DDIM]")
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