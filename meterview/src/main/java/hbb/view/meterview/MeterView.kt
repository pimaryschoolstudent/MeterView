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
import hbb.view.meterview.R

/**
 * @author HuangJiaHeng
 * @date 2019/11/12.
 */
class MeterView : FrameLayout{
   companion object{
       const val SOURCE_CENTER = 0
       const val SOURCE_START = 1
       const val SOURCE_END = 2
   }
    private var boardPaint: Paint ?= null
    private var scaleTextPaint: Paint ?= null
    private var sourcePaint:Paint ?= null
    private var sourceArcPaint:Paint ?= null
    private var pointPaint:Paint ?= null
    private var source:Float = 0f
    private var degrees = -150f
    private var baseView:BoadrView ?= null
    private var attrs:AttributeSet ?= null

    private lateinit var dataManager:MeterDataManager

    constructor(context: Context,attrs:AttributeSet ) : super(context,attrs){
        addChildView(context,attrs)
        this.attrs = attrs
        checkStyle(attrs)
        initPaint()
    }

    fun runIndex(index:Int){
        var arrayIndex = index
        if (arrayIndex>dataManager.ScaleTextArray.size-1){
            arrayIndex = dataManager.ScaleTextArray.size-1
        }else if(arrayIndex<0){
            arrayIndex = 0
        }
        var targetS = 100 * arrayIndex/(dataManager.ScaleTextArray.size-1)
        Thread(Runnable {
            while(true){
                source +=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("runsS","targetS$targetS source $source")
                if (source.toInt()==targetS.toInt()){
                    break
                }
                baseView?.postInvalidate()
            }
        }).start()
    }

    fun runSource(runSource:Int){
        source=0f
        var targetS = runSource
        if (targetS<0) {
            targetS = 0
        }else if(targetS>100){
            targetS = 100
        }
        Thread(Runnable {
            while(true){
                source +=0.1f
                Thread.sleep((100/dataManager.Rate).toLong())
                Log.e("runsS","targetS$targetS source $source")
                if (source.toInt()==targetS){
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

        dataManager.ScaleTextInterval = ta.getDimension(R.styleable.MeterView_ScaleTextInterval,dp2Px(25f))
        if ( dataManager.ScaleTextInterval<dp2Px(25f)){
            dataManager.ScaleTextInterval = dp2Px(25f)
        }

        dataManager.SourceTextInterval = ta.getDimension(R.styleable.MeterView_SourceTextInterval,-1f)
        dataManager.SourceTextAlign = ta.getInt(R.styleable.MeterView_SourceTextAlign,-1)

        dataManager.Rate = ta.getInt(R.styleable.MeterView_Rate,20)

        dataManager.ShowSource = ta.getBoolean(R.styleable.MeterView_ShowSource,true)

        dataManager.startAngle = ta.getFloat(R.styleable.MeterView_StartAnglee,-215f)
        dataManager.sweepAngle = ta.getFloat(R.styleable.MeterView_SweepAnglee,250f)
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
//            drawDebugRect(canvas,rect)
            drawBg(canvas, rect)
            drawSourceArc(canvas,rect)
            drawSourcePoint(canvas,rect)
            drawAllScaleText(canvas)
            drawSourceText(canvas)
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

       private fun drawAllScaleText(canvas: Canvas?){

           degrees = (90+dataManager.startAngle)
           for ( i in dataManager.ScaleTextArray.indices){
               if ((source/(100/(dataManager.ScaleTextArray.size-1))).toInt() == i){
                   scaleTextPaint?.color = dataManager.CheckScaleTextColor
                   scaleTextPaint?.textSize = dataManager.CheckScaleTextSize
               }else{
                   scaleTextPaint?.color =dataManager.ScaleTextColor
                   scaleTextPaint?.textSize = dataManager.ScaleTextSize
               }
               drawScaleText(canvas,degrees,dataManager.ScaleTextArray[i])
               degrees += dataManager.sweepAngle/(dataManager.ScaleTextArray.size-1)
           }
       }

       private fun drawDebugRect(canvas: Canvas?,rect: RectF){
           canvas?.drawRect(rect,boardPaint)
           canvas?.drawRect(RectF( 0f, 0f,width.toFloat(),height.toFloat()),boardPaint)
       }

       private fun drawSourceArc(canvas: Canvas?,rect: RectF){
           canvas?.drawArc(rect,dataManager.startAngle,source*dataManager.sweepAngle/100,false,sourceArcPaint)
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
           var sourceDegrees = (dataManager.sweepAngle*source)/100 + (90+dataManager.startAngle)
           canvas?.rotate(sourceDegrees,rect.centerX(),rect.centerY())
           canvas?.drawCircle(rect.centerX(),rect.top,dataManager.PointSize,pointPaint)
           canvas?.rotate(-sourceDegrees,rect.centerX(),rect.centerY())
       }

       private fun drawSourceText(canvas: Canvas?){
           if(dataManager.ShowSource){
               if (dataManager.SourceTextAlign == -1){
                   if (dataManager.SourceTextInterval != -1f){
                       canvas?.drawText(source.toInt().toString(),width/2 - sourcePaint?.measureText(source.toInt().toString())!!/2,dataManager.SourceTextInterval-sourcePaint?.measureText(source.toInt().toString())!!/2,sourcePaint)
                   }else{
                       canvas?.drawText(source.toInt().toString(),width/2 - sourcePaint?.measureText(source.toInt().toString())!!/2,height/2f,sourcePaint)
                   }
               }
               when(dataManager.SourceTextAlign){
                   0 -> canvas?.drawText(source.toInt().toString(),width/2 - sourcePaint?.measureText(source.toInt().toString())!!/2,height/2f,sourcePaint)
                   1 -> canvas?.drawText(source.toInt().toString(),width/2 - sourcePaint?.measureText(source.toInt().toString())!!/2,dataManager.ScaleTextInterval+dataManager.CheckScaleTextSize+10,sourcePaint)
                   2 -> canvas?.drawText(source.toInt().toString(),width/2 - sourcePaint?.measureText(source.toInt().toString())!!/2,height-sourcePaint?.measureText(source.toInt().toString())!!/2,sourcePaint)
               }
           }
       }
       private fun drawScaleText(canvas: Canvas?,degrees:Float,text:String){
           canvas?.rotate(degrees,width/2f,height/2f)
           canvas?.drawText(text,width/2 - scaleTextPaint?.measureText(text)!!/2,dataManager.ScaleTextInterval,scaleTextPaint)
           canvas?.rotate(-degrees,width/2f,height/2f)
       }
    }


}