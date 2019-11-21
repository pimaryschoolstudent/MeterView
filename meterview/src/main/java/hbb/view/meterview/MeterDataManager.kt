package hbb.view.meterview

import android.graphics.Paint
import java.util.ArrayList

/**
 * @author HuangJiaHeng
 * @date 2019/11/13.
 */
data class MeterDataManager (
    var boardPaint:Paint?=null,
    var scaleTextPaint:Paint?=null,
    var sourcePaint:Paint?=null,
    var sourceArcPaint:Paint?=null,
    var pointPaint:Paint?=null,


    var ScaleTextSize:Float = 0f,
    var SourceTextSize:Float= 0f,
    var CheckScaleTextSize:Float = 0f,
    var PointSize:Float = 0f,
    var SourceCurveSize: Float = 0f,
    var BoardCurveSize: Float = 0f,

    var ScaleTextInterval:Float =0f,
    var SourceTextInterval:Float = 0f,

    var SourceTextAlign:Int = 0,
    var Rate:Int = 0,

    var ScaleTextArray:ArrayList<String> = arrayListOf("10","20","30","40","50","60","70","80","90","100"),

    var ShowSource:Boolean = true,
    var startAngle:Float = -215f,
    var sweepAngle:Float = 250f
){
    var DashBoardColor:Int = 0
        set(value) {
            field = value
            boardPaint?.color = value
        }
    var ScaleTextColor:Int = 0
        set(value) {
            field = value
            scaleTextPaint?.color = value
        }
    var CheckScaleTextColor:Int = 0
        set(value) {
            field = value
            scaleTextPaint?.color = value
        }
    var SourceCurveColor:Int= 0
        set(value) {
            field = value
            sourceArcPaint?.color = value
        }
    var SourceTextColor:Int = 0
        set(value) {
            field = value
            sourcePaint?.color = value
        }
    var PointColor:Int = 0
        set(value) {
            field = value
            pointPaint?.color = value
        }
}