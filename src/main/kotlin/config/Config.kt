package com.hcyacg.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

public object Config : AutoSavePluginConfig("config") {
    @ValueName("username")
    @ValueDescription("登录用户名")
    public var username :String by value()

    @ValueName("password")
    @ValueDescription("登录密码")
    public var password :String by value()

    @ValueName("owner")
    @ValueDescription("管理员")
    public var owner: MutableList<String> by value()

    @ValueName("stableDiffusionWebui")
    @ValueDescription("stable-diffusion-webui接口")
    public var stableDiffusionWebui :String by value("http://127.0.0.1:7860")

    @ValueName("textFnIndex")
    @ValueDescription("接口id")
    public var textFnIndex:Int by value(13)

    @ValueName("imageFnIndex")
    @ValueDescription("接口id")
    public var imageFnIndex:Int by value(33)

    @ValueName("mode")
    @ValueDescription("模式，仅限image2image")
    public var mode:Int by value(0)

    @ValueName("initImgWithMask")
    @ValueDescription("[暂时不清楚该填什么]原始图蒙版模式，仅限image2image")
    public var initImgWithMask:Int by value(-1)

    @ValueName("initImgInPaint")
    @ValueDescription("[暂时不清楚该填什么]原始图图层，仅限image2image")
    public var initImgInPaint:Int by value(-1)

    @ValueName("initMaskInPaint")
    @ValueDescription("[暂时不清楚该填什么]原始蒙版图层，仅限image2image")
    public var initMaskInPaint:Int by value(-1)

    @ValueName("maskMode")
    @ValueDescription("蒙版模式，仅限image2image,[Upload mask|Draw mask]")
    public var maskMode:String by value("Draw mask")

    @ValueName("maskBlur")
    @ValueDescription("模糊遮罩滤镜，仅限image2image")
    public var maskBlur:Int by value(4)

    @ValueName("inPaintingFill")
    @ValueDescription("修复填充，仅限image2image，[fill|original|latent noise|latent nothing]")
    public var inPaintingFill:String by value("original")

    @ValueName("resizeMode")
    @ValueDescription("尺寸调整模式，仅限image2image [Crop and resize|Just resize|Resize and fill]")
    public var resizeMode:String by value("Just resize")

    @ValueName("inPaintFullRes")
    @ValueDescription("图层全分辨率模式，仅限image2image")
    public var inPaintFullRes:Boolean by value(false)

    @ValueName("inPaintFullResPadding")
    @ValueDescription("图层全分辨率填充，仅限image2image")
    public var inPaintFullResPadding:Int by value(32)

    @ValueName("inPaintingMaskInvert")
    @ValueDescription("图层模板倒置，仅限image2image,[Inpaint not masked|Inpaint masked]")
    public var inPaintingMaskInvert:String by value("Inpaint masked")

    @ValueName("translated")
    @ValueDescription("翻译词条")
    public var translated: Boolean by value(true)

    @ValueName("negativePrompt")
    @ValueDescription("过滤词条")
    public var negativePrompt: String by value("multiple breasts, bad anatomy, liquid body, liquid tongue, disfigured, mutated, anatomical nonsense, text font ui, error, malformed hands, long neck, blurred, lowers,bad anatomy, bad proportions, bad shadow, uncoordinated body, unnatural body, fused breasts, bad breasts, huge breasts, poorly drawn breasts, extra breasts, liquid breasts, missing breasts, huge haunch, huge thighs, huge calf, bad hands, fused hand, missing hand, disappearing arms, disappearing thigh, disappearing calf, disappearing legs, fused ears, bad ears, poorly drawn ears, extra ears, liquid ears, heavy ears, missing ears, fused animal ears, bad animal ears, poorly drawn animal ears, extra animal ears, liquid animal ears, heavy animal earsblurry, JPEG artifacts, signature, 3D, 3D game, 3D game scene, 3D character, malformed feet, extra feet, bad feet, poorly drawn feet, fused feet, missing feet,extra shoes, bad shoes, fused shoes, more than two shoes, poorly drawn shoes, bad gloves, poorly drawn gloves, fused gloves, bad cum, poorly drawn cum, fused cum, bad hairs, poorly drawn hairs, fused hairs,bad eyes, fused eyes poorly drawn eyes, extra eyes, malformed limbs, more than 2 nipples, missing nipples, different nipples, fused nipples, bad nipples, poorly drawn nipples, black nipples, colorful nipples, gross proportions. short arm, (((missing arms))), missing thighs, missing calf, missing legs, mutation, duplicate, morbid, mutilated, poorly drawn hands, more than 1 left hand, more than 1 right hand, deformed, (blurry), disfigured, missing legs, extra arms, extra thighs, more than 2 thighs, extra calf, fused calf, extra legs, bad knee, extra knee, more than 2 legs, bad tails, bad mouth, fused mouth, poorly drawn mouth, bad tongue, tongue within mouth, too long tongue, black tongue,big mouth, cracked mouth, bad mouth, dirty face, dirty teeth, dirty pantie, fused pantie, poorly drawn pantie, fused cloth, poorly drawn cloth, bad pantie, yellow teeth, thick lips, bad cameltoe, colorful cameltoe, bad asshole, poorly drawn asshole, fused asshole, missing asshole, bad anus, bad pussy, bad crotch, bad crotch seam, fused anus, fused pussy, fused anus, fused crotch, poorly drawn crotch, fused seam, poorly drawn anus, poorly drawn pussy, poorly drawn crotch, poorly drawn crotch seam, bad thigh gap, missing thigh gap, fused thigh gap, liquid thigh gap, poorly drawn thigh gap, poorly drawn anus, bad collarbone, fused collarbone, missing collarbone, liquid collarbone, strong girl, obesity, worst quality, low quality, normal quality, liquid tentacles, bad tentacles, poorly drawn tentacles, split tentacles, fused tentacles")

    @ValueName("promptStyle")
    @ValueDescription("词条风格")
    public var promptStyle: String by value("None")

    @ValueName("promptStyle2")
    @ValueDescription("词条风格2")
    public var promptStyle2: String by value("None")

    @ValueName("steps")
    @ValueDescription("步骤")
    public var steps: Int by value(40)

    @ValueName("samplerIndex")
    @ValueDescription("采样器索引,Euler a|Euler|LMS|Heun|DPM2|DPM2 a|DPM fast|DPM adaptive|LMS Karras|DPM2 Karras|DPM2 a Karras|DDIM|PLMS, 图片转图片[Euler a|Euler|LMS|Heun|DPM2|DPM2 a|LMS Karras|DPM2 Karras|DPM2 a Karras|DDIM]")
    public var samplerIndex: String by value("Euler a")

    @ValueName("restoreFaces")
    @ValueDescription("恢复面")
    public var restoreFaces: Boolean by value(false)

    @ValueName("tiling")
    @ValueDescription("生成一个可以平铺的图像")
    public var tiling: Boolean by value(false)

    @ValueName("nIter")
    @ValueDescription("尝试运行多少次")
    public var nIter: Int by value(1)

    @ValueName("batchSize")
    @ValueDescription("在单个批处理中创建多少图像")
    public var batchSize: Int by value(1)

    @ValueName("cfgScale")
    @ValueDescription("分类器自由引导尺度——图像与提示符的一致程度——越低的值产生越有创意的结果")
    public var cfgScale: Float by value(7f)

    @ValueName("seed")
    @ValueDescription("随机种子，-1为随机 只限数字")
    public var seed: Long by value(-1L)

    @ValueName("subSeed")
    @ValueDescription("候补种子 只限数字")
    public var subSeed: Int by value(-1)

    @ValueName("subSeedStrength")
    @ValueDescription("候补种子强度")
    public var subSeedStrength: Float by value(0f)

    @ValueName("seedResizeFromH")
    @ValueDescription("种子从高调大小")
    public var seedResizeFromH: Int by value(0)

    @ValueName("seedResizeFromW")
    @ValueDescription("种子从宽调大小")
    public var seedResizeFromW: Int by value(0)

    @ValueName("seedEnableExtras")
    @ValueDescription("种子启用额外部分")
    public var seedEnableExtras: Boolean by value(false)

    @ValueName("height")
    @ValueDescription("高度")
    public var height: Int by value(512)

    @ValueName("width")
    @ValueDescription("宽度")
    public var width: Int by value(512)

    @ValueName("enableHr")
    @ValueDescription("Highres.fix 渲染两次")
    public var enableHr: Boolean by value(false)

    @ValueName("scaleLatent")
    @ValueDescription("潜在的尺度")
    public var scaleLatent: Boolean by value(false)

    @ValueName("denoisingStrength")
    @ValueDescription("与原图的差异度")
    public var denoisingStrength: Float by value(0.7f)

    @ValueName("script")
    @ValueDescription("脚本,Prompt matrix|Prompts from file or textbox|X/Y plot")
    public var script:String by value("None")

    @ValueName("xtype")
    @ValueDescription("只有脚本选择X/Y plot后使用")
    public var xtype:String by value("Seed")

    @ValueName("xvalues")
    @ValueDescription("只有脚本选择X/Y plot后使用，否则默认为\"\"")
    public var xvalues:String by value("")

    @ValueName("ytype")
    @ValueDescription("只有脚本选择X/Y plot后使用，否则默认为\"\"")
    public var ytype:String by value("Nothing")

    @ValueName("yvalues")
    @ValueDescription("只有脚本选择X/Y plot后使用，否则默认为\"\"")
    public var yvalues:String by value("")

    @ValueName("drawLegend")
    @ValueDescription("画的传说？搞不懂")
    public var drawLegend: Boolean by value(true)

    @ValueName("keepRandomSeeds")
    @ValueDescription("保持随机")
    public var keepRandomSeeds : Boolean by value(false)

    @ValueName("putVariablePartsAtStartOfPrompt")
    @ValueDescription("在词条的开始处放置可变部件")
    public var putVariablePartsAtStartOfPrompt: Boolean by value(false)
}