package hbb.view.meterview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import java.math.BigDecimal
import java.math.RoundingMode


/**
 * @author HuangJiaHeng
 * @date 2019/11/12.
 */
class MeterView : FrameLayout{
   companion object{
       const val SOURCE_CENTER = 0
       const val SOURCE_START = 1
       const val SOURCE_END = 2
       const val SOURCE_NUMBER = 0
       const val SOURCE_SCALETEXT =1
   }
    private var boardPaint: Paint ?= null
    private var scaleTextPaint: Paint ?= null
    private var sourcePaint:Paint ?= null
    private var sourceArcPaint:Paint ?= null
    private var pointPaint:Paint ?= null
    private var source:Float = 0f
    private var currentIndex:Int = 0
    private var degrees = -150f
    private var baseView:BoadrView ?= null
    private var attrs:AttributeSet ?= null
    private var sourceChangeListener:SourceChangeListener ?=null
    private var lastSource:Int = -1
    private var currentCheckScaleText:String = ""
    private lateinit var dataManager:MeterDataManager

    constructor(context: Context,attrs:AttributeSet ) : super(context,attrs){
        addChildView(context,attrs)
        this.attrs = attrs
        checkStyle(attrs)
        initPaint()
    }

    fun runIndex(index:Int){
        lastSource= -1
        source = 0f
        currentIndex = index
        if (currentIndex>dataManager.ScaleTextArray.size-1){
            currentIndex = dataManager.ScaleTextArray.size-1
        }else if(currentIndex<0){
            currentIndex = 0
        }
        var targetS = getBigDecimal(1,100f * currentIndex/(dataManager.ScaleTextArray.size-1))
        Thread(Runnable {
            while(true){
                source +=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("runsS","targetS$targetS source ${getSource(1)}")
                if (lastSource!=getSource(1).toInt()){
                    sourceChangeListener?.SourceChange(getSource(1).toInt(),currentCheckScaleText)
                }
                lastSource = source.toInt()
                if (getSource(1)==targetS || source.toInt() == 100 ){
                    break
                }

                baseView?.postInvalidate()
            }
        }).start()
    }

    fun addIndex(index: Int){
        var targetS =  getBigDecimal(1,source+100f * index/(dataManager.ScaleTextArray.size-1))
        Thread(Runnable {
            while(true){
                source +=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("runsS","targetS$targetS source ${getSource(1)}")
                if (lastSource!=getSource(1).toInt()){
                    sourceChangeListener?.SourceChange(getSource(1).toInt(),currentCheckScaleText)
                }
                lastSource = source.toInt()
                if (getSource(1)==targetS || source.toInt() == 100 ){
                    break
                }

                baseView?.postInvalidate()
            }
        }).start()
    }

    fun runSource(runSource:Int){
        source=0f
        lastSource= -1
        var targetS = runSource.toFloat()
        if (targetS<0) {
            targetS = 0f
        }else if(targetS>100){
            targetS = 100f
        }
        Thread(Runnable {
            while(true){
                Thread.sleep((100/dataManager.Rate).toLong())
                source +=0.1f
                if (lastSource!=getSource(1).toInt()){
                    sourceChangeListener?.SourceChange(getSource(1).toInt(),currentCheckScaleText)
                }
                lastSource = source.toInt()
                Log.e("runsS","targetS${targetS} source ${getSource(1)}")
                if (getSource(1)==targetS || source.toInt() == 100 ){
                    break
                }
                baseView?.postInvalidate()
            }
        }).start()
    }

    fun addSource(runSource: Int){
        var targetS = source + runSource.toFloat()
        Thread(Runnable {
            while(true){
                source +=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("addSource","targetS${getBigDecimal(1,targetS)} source ${getSource(1)}")
                if (lastSource!=getSource(1).toInt()){
                    sourceChangeListener?.SourceChange(getSource(1).toInt(),currentCheckScaleText)
                }
                lastSource = source.toInt()
                if (getSource(1)==getBigDecimal(1,targetS) || source.toInt() == 100 ){
                    break
                }
                baseView?.postInvalidate()
            }
        }).start()
    }

    fun subtractSource(runSource:Int){
        var targetS = source - runSource
        Thread(Runnable {
            while(true){
                source -=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("runsS","targetS${getBigDecimal(1,targetS)} source ${getSource(1)}")
                if (lastSource!=getSource(1).toInt()){
                    sourceChangeListener?.SourceChange(getSource(1).toInt(),currentCheckScaleText)
                }
                lastSource = source.toInt()
                if (getSource(1) == getBigDecimal(1,targetS) || source <= 0 ){
                    break
                }
                baseView?.postInvalidate()
            }
        }).start()
    }
    fun subtractIndex(index:Int){
        var targetS = getBigDecimal(1,source - 100f * index/(dataManager.ScaleTextArray.size-1))
        Thread(Runnable {
            while(true){
                source -=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("runsS","targetS$targetS source ${getSource(1)}")
                if (lastSource!=getSource(1).toInt()){
                    sourceChangeListener?.SourceChange(getSource(1).toInt(),currentCheckScaleText)
                }
                lastSource = source.toInt()
                if (getSource(1) == targetS || source <= 0  ){
                    break
                }
                baseView?.postInvalidate()
            }
        }).start()
    }

    fun  setScaleArray(data:ArrayList<String>){
        dataManager.ScaleTextArray = data
        baseView?.postInvalidate()
    }

    fun getDataManager():MeterDataManager{
        return dataManager
    }

    fun setSourceChangeListener(sourceChangeListener: SourceChangeListener){
        this.sourceChangeListener = sourceChangeListener
    }

    private fun getSource(newScale:Int):Float{
        var bd = BigDecimal(source.toDouble())
        bd = bd.setScale(newScale, RoundingMode.HALF_UP)
        return bd.toFloat()
    }

    private fun getBigDecimal(newScale:Int,data:Float):Float{
        var bd = BigDecimal(data.toDouble())
        bd = bd.setScale(newScale, RoundingMode.HALF_UP)
        return bd.toFloat()
    }

    private fun checkStyle(attrs: AttributeSet){
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MeterView)
        dataManager = MeterDataManager()
        dataManager.DashBoardColor =  ta.getColor(R.styleable.MeterView_DashBoardColor,Color.parseColor("#66B5FF"))
        dataManager.CheckScaleTextColor =  ta.getColor(R.styleable.MeterView_CheckScaleTextColor,Color.parseColor("#66B5FF"))
        dataManager.ScaleTextColor =  ta.getColor(R.styleable.MeterView_ScaleTextColor,Color.parseColor("#66B5FF"))
        dataManager.SourceCurveColor =  ta.getColor(R.styleable.MeterView_SourceCurveColor,Color.parseColor("#000000"))
        dataManager.SourceTextColor =  ta.getColor(R.styleable.MeterView_SourceTextColor,Color.parseColor("#66B5FF"))
        dataManager.PointColor =  ta.getColor(R.styleable.MeterView_PointColor,Color.parseColor("#FF0000"))

        dataManager.ScaleTextSize = ta.getDimension(R.styleable.MeterView_ScaleTextSize,dp2Px(14f))
        dataManager.SourceTextSize = ta.getDimension(R.styleable.MeterView_SourceTextSize,dp2Px(30f) )
        dataManager.CheckScaleTextSize = ta.getDimension(R.styleable.MeterView_CheckScaleTextSize,dp2Px(18f))

        dataManager.BoardCurveSize = ta.getDimension(R.styleable.MeterView_BoardCurveSize,dp2Px(2f))
        dataManager.SourceCurveSize = ta.getDimension(R.styleable.MeterView_SourceCurveSize,dp2Px(5f))
        dataManager.PointSize = ta.getDimension(R.styleable.MeterView_PointSize,dp2Px(5f))

        dataManager.ScaleTextInterval = ta.getDimension(R.styleable.MeterView_ScaleTextInterval,dp2Px(0f))

        dataManager.SourceTextInterval = ta.getDimension(R.styleable.MeterView_SourceTextInterval,-1f)
        dataManager.SourceTextAlign = ta.getInt(R.styleable.MeterView_SourceTextAlign,-1)

        dataManager.Rate = ta.getInt(R.styleable.MeterView_Rate,20)

        dataManager.ShowSource = ta.getBoolean(R.styleable.MeterView_ShowSource,true)
        dataManager.ShowCheckScaleText = ta.getBoolean(R.styleable.MeterView_ShowCheckScaleText,true)
        dataManager.ShowPoint = ta.getBoolean(R.styleable.MeterView_ShowPoint,true)
        dataManager.ShowScaleText = ta.getBoolean(R.styleable.MeterView_ShowScaleText,true)
        dataManager.ShowSourceCurve = ta.getBoolean(R.styleable.MeterView_ShowSourceCurve,true)


        dataManager.startAngle = ta.getFloat(R.styleable.MeterView_StartAnglee,-215f)
        dataManager.sweepAngle = ta.getFloat(R.styleable.MeterView_SweepAnglee,250f)

        dataManager.sourceShowType = ta.getInt(R.styleable.MeterView_SourceShowType,0)
    }

    private fun initPaint(){
        boardPaint = Paint()
        boardPaint?.color=dataManager.DashBoardColor
        boardPaint?.isAntiAlias=true
        boardPaint?.style=Paint.Style.STROKE

        scaleTextPaint = Paint()
        scaleTextPaint?.color = dataManager.ScaleTextColor
        scaleTextPaint?.isAntiAlias=true

        sourcePaint = Paint()
        sourcePaint?.color = dataManager.SourceTextColor
        sourcePaint?.isAntiAlias=true

        sourceArcPaint = Paint()
        sourceArcPaint?.color =  dataManager.SourceCurveColor
        sourceArcPaint?.isAntiAlias=true
        sourceArcPaint?.style = Paint.Style.STROKE

        pointPaint = Paint()
        pointPaint?.color =dataManager.PointColor
        pointPaint?.isAntiAlias=true

        dataManager.boardPaint = boardPaint
        dataManager.scaleTextPaint = scaleTextPaint
        dataManager.sourcePaint = sourcePaint
        dataManager.sourceArcPaint = sourceArcPaint
        dataManager.pointPaint = pointPaint
    }

    private fun addChildView(context: Context,attrs: AttributeSet){
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        baseView = BoadrView(context,attrs)
        addView(baseView,layoutParams)
    }

    fun px2Dp(px:Float):Float {
        return px/context.resources.displayMetrics.density

    }

    fun dp2Px(dp:Float):Float{
        return dp*context.resources.displayMetrics.density
    }

   private inner class BoadrView(context:Context,attrs: AttributeSet): View(context,attrs) {
        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            canvas?.save()

            var rect = checkRecfF()
            initWidth()
            if (dataManager.meterDebug){
                drawDebugRect(canvas,rect)
            }
            drawBg(canvas, rect)
            drawSourceArc(canvas,rect)
            drawSourcePoint(canvas,rect)
            drawAllScaleText(canvas,rect)
            drawSourceText(canvas,rect)
            canvas?.restore()
        }

       private fun checkRecfF():RectF{
           //判断分数弧的大小和圆点大小，防止圆点或者圆弧越界，打开debug视图看得更加清楚
           if (dataManager.SourceCurveSize/2>dataManager.PointSize){
               return RectF( dataManager.SourceCurveSize/2, dataManager.SourceCurveSize/2,width.toFloat()- dataManager.SourceCurveSize/2,height.toFloat()- dataManager.SourceCurveSize/2)
           }else{
               return RectF( dataManager.PointSize, dataManager.PointSize,width.toFloat()- dataManager.PointSize,height.toFloat()- dataManager.PointSize)
           }
       }

       private fun drawAllScaleText(canvas: Canvas?,rect: RectF){
           degrees = (90+dataManager.startAngle)
           for ( i in dataManager.ScaleTextArray.indices){
               if (getBigDecimal(1,source/(100/(dataManager.ScaleTextArray.size-1))).toInt() == i){
                   scaleTextPaint?.color = dataManager.CheckScaleTextColor
                   scaleTextPaint?.textSize = dataManager.CheckScaleTextSize
                   if (currentCheckScaleText != dataManager.ScaleTextArray[i]){
                       sourceChangeListener?.CheckScaleTextChange(dataManager.ScaleTextArray[i])
                   }
                   currentCheckScaleText = dataManager.ScaleTextArray[i]
                   if (dataManager.ShowCheckScaleText){
                       drawScaleText(canvas,degrees,dataManager.ScaleTextArray[i],rect)
                   }
               }else{
                   scaleTextPaint?.color =dataManager.ScaleTextColor
                   scaleTextPaint?.textSize = dataManager.ScaleTextSize
                   if (dataManager.ShowScaleText){
                       drawScaleText(canvas,degrees,dataManager.ScaleTextArray[i],rect)
                   }
               }

               degrees += dataManager.sweepAngle/(dataManager.ScaleTextArray.size-1)
           }
       }


       private fun drawDebugRect(canvas: Canvas?,rect: RectF){
           canvas?.drawRect(rect,boardPaint)
           canvas?.drawRect(RectF( 0f, 0f,width.toFloat(),height.toFloat()),boardPaint)
       }

       private fun drawSourceArc(canvas: Canvas?,rect: RectF){
           if (dataManager.ShowSourceCurve){
               canvas?.drawArc(rect,dataManager.startAngle,source*dataManager.sweepAngle/100,false,sourceArcPaint)
           }
       }

       private fun drawBg(canvas: Canvas?,rect: RectF){
           canvas?.drawArc( rect,dataManager.startAngle,dataManager.sweepAngle,false,boardPaint)
       }

       private fun initWidth(){
           scaleTextPaint?.textSize = dataManager.ScaleTextSize
           boardPaint?.strokeWidth = dataManager.BoardCurveSize
           sourcePaint?.textSize = dataManager.SourceTextSize
           sourceArcPaint?.strokeWidth = dataManager.SourceCurveSize
       }

       private fun drawSourcePoint(canvas: Canvas?,rect:RectF){
           if (dataManager.ShowPoint){
               var sourceDegrees = (dataManager.sweepAngle*source)/100 + (90+dataManager.startAngle)
               canvas?.rotate(sourceDegrees,rect.centerX(),rect.centerY())
               canvas?.drawCircle(rect.centerX(),rect.top,dataManager.PointSize,pointPaint)
               canvas?.rotate(-sourceDegrees,rect.centerX(),rect.centerY())
           }
       }

       private fun drawSourceText(canvas: Canvas?,rect: RectF){
           if(dataManager.ShowSource){
               var sourceText = getSource(1).toInt().toString()
               if (dataManager.sourceShowType == 1){
                       sourceText = currentCheckScaleText
               }
               var textHeight = sourcePaint!!.descent()-sourcePaint!!.ascent()
               var scaleTextHeight = scaleTextPaint!!.descent()-scaleTextPaint!!.ascent()

               if (dataManager.SourceTextAlign == -1){
                   if (dataManager.SourceTextInterval != -1f){
                       canvas?.drawText(sourceText,width/2 - sourcePaint?.measureText(sourceText)!!/2,dataManager.SourceTextInterval+textHeight/2+scaleTextHeight+dataManager.ScaleTextInterval+ (dataManager.CheckScaleTextSize-dataManager.ScaleTextSize)/2+ rect.top+scaleTextHeight/2+if (dataManager.SourceCurveSize>dataManager.PointSize) dataManager.SourceCurveSize/2 else dataManager.PointSize,sourcePaint)
                   }else{
                       canvas?.drawText(sourceText,width/2 - sourcePaint?.measureText(sourceText)!!/2,height/2f,sourcePaint)
                   }
               }
               /**
                *  0:居中
                *  1:开始
                *  2:结尾
                *  */
               when(dataManager.SourceTextAlign){
                   0 -> canvas?.drawText(sourceText,width/2 - sourcePaint?.measureText(sourceText)!!/2,height/2f,sourcePaint)
                   1 -> canvas?.drawText(sourceText,width/2 - sourcePaint?.measureText(sourceText)!!/2,textHeight/2+scaleTextHeight+dataManager.ScaleTextInterval+ (dataManager.CheckScaleTextSize-dataManager.ScaleTextSize)/2+ rect.top+scaleTextHeight/2+if (dataManager.SourceCurveSize>dataManager.PointSize) dataManager.SourceCurveSize/2 else dataManager.PointSize,sourcePaint)
                   2 -> canvas?.drawText(sourceText,width/2 - sourcePaint?.measureText(sourceText)!!/2,height.toFloat(),sourcePaint)
               }
           }
       }
       private fun drawScaleText(canvas: Canvas?,degrees:Float,text:String,rect: RectF){
           var textHeight = scaleTextPaint!!.descent()-scaleTextPaint!!.ascent()
           canvas?.rotate(degrees,width/2f,height/2f)
           //高度需加上内矩形的高，圆点或者分数弧的高（看哪个大），选中文字大小和普通文字大小的差
           canvas?.drawText(text,width/2 - scaleTextPaint?.measureText(text)!!/2,dataManager.ScaleTextInterval+(dataManager.CheckScaleTextSize-dataManager.ScaleTextSize)/2+ rect.top+textHeight/2+if (dataManager.SourceCurveSize>dataManager.PointSize) dataManager.SourceCurveSize else dataManager.PointSize,scaleTextPaint)
           canvas?.rotate(-degrees,width/2f,height/2f)
       }
    }

    interface SourceChangeListener{
        fun SourceChange(source:Int,currentText: String)
        fun CheckScaleTextChange(currentText:String)
    }

}