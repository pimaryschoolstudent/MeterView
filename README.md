MeterView

![](https://jitpack.io/v/pimaryschoolstudent/MeterView.svg)

添加依赖库

项目目录——>build.gradle

	allprojects {

		repositories {
		
			...
			
			maven { url 'https://jitpack.io' }
			
		}
		
	}

	
app——>build.gradle
  
  	dependencies {
  
       		...
       
	     implementation 'com.github.pimaryschoolstudent:MeterView:1.0.0'
	     
	}
	
组件使用

	var arrayList:ArrayList<String> = ArrayList()
	
        arrayList.add("0")
        arrayList.add("10")
        arrayList.add("20")
        arrayList.add("30")
        arrayList.add("40")
        arrayList.add("较差")
        arrayList.add("中等")
        arrayList.add("良好")
        arrayList.add("优秀")
        meter.setScaleArray(arrayList)

        //设置数据
        //颜色
        meter.getDataManager().ScaleTextColor = Color.BLUE       //底盘弧刻度文字颜色
        meter.getDataManager().DashBoardColor = Color.BLUE       //底盘弧度颜色
        meter.getDataManager().PointColor = Color.RED            //分数点颜色
        meter.getDataManager().SourceTextColor = Color.RED       //分数颜色
        meter.getDataManager().SourceCurveColor = Color.RED      //分数弧颜色
        meter.getDataManager().CheckScaleTextColor = Color.BLACK //选中分数刻度颜色

        //大小 dp2px（dp转px）  px2dp（px转dp）
        meter.getDataManager().CheckScaleTextSize  = meter.dp2Px(15f)  //选中分数刻度文字大小
        meter.getDataManager().ScaleTextSize = meter.dp2Px(10f)        //刻度文字大小
        meter.getDataManager().SourceTextSize = meter.dp2Px(50f)       //分数文字大小

        meter.getDataManager().PointSize = meter.dp2Px(8f)             //分数点直径大小
        meter.getDataManager().SourceCurveSize = meter.dp2Px(10f)      //分数弧宽度大小
        meter.getDataManager().BoardCurveSize = meter.dp2Px(5f)        //底盘弧宽度大小

        meter.getDataManager().SourceTextInterval = meter.dp2Px(20.5f)  //分数离顶部的间隔大小

        //其他设置
        meter.getDataManager().Rate = 20    //分数弧动画速率（20/s）

        meter.getDataManager().ShowSource = true //是否显示分数

        meter.getDataManager().SourceTextAlign = MeterView.SOURCE_CENTER    //分数居中方式 SOURCE_CENTER（底盘居中） OURCE_START（底盘顶部） SOURCE_END （底盘底部）

        meter.getDataManager().startAngle = -180f   //底盘弧开始点的角度（起点为组件最右边点，当等于-90时为组件中点）
        meter.getDataManager().sweepAngle = 180f    //底盘弧角度大小

        //跑分
        meter.runIndex(4) //根据数据索引跑分 （不为-1，不大于数据大小）
	//meter.runSource(100) //根据分数跑分（百分制，大于100则等于100）
