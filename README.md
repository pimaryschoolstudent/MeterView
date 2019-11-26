MeterView

![](https://jitpack.io/v/pimaryschoolstudent/MeterView.svg)

效果展示

![Image text](https://github.com/pimaryschoolstudent/MeterView/blob/master/showImage/-180180.gif)
![Image text](https://github.com/pimaryschoolstudent/MeterView/blob/master/showImage/-180325.gif)

更多UI效果

![Image text](https://github.com/pimaryschoolstudent/MeterView/blob/master/showImage/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20191126110912.png)

![Image text](https://github.com/pimaryschoolstudent/MeterView/blob/master/showImage/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20191126111029.png)

![Image text](https://github.com/pimaryschoolstudent/MeterView/blob/master/showImage/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20191126111043.png)

美感不是很好~~~

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
       
	     implementation 'com.github.pimaryschoolstudent:MeterView:1.0.1'
	     
	}
	
组件使用

       //添加数据
        var arrayList:ArrayList<String> = ArrayList()
        arrayList.add("0")
        arrayList.add("10")
        arrayList.add("20")
        arrayList.add("30")
        arrayList.add("40")
        arrayList.add("50")
        arrayList.add("较差")
        arrayList.add("中等")
        arrayList.add("良好")
        arrayList.add("优秀")
        arrayList.add("100")
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
        meter.getDataManager().SourceTextSize = meter.dp2Px(40f)       //分数文字大小

        meter.getDataManager().PointSize = meter.dp2Px(5f)             //分数点半径大小
        meter.getDataManager().SourceCurveSize = meter.dp2Px(10f)      //分数弧宽度大小
        meter.getDataManager().BoardCurveSize = meter.dp2Px(5f)        //底盘弧宽度大小

        meter.getDataManager().SourceTextInterval = meter.dp2Px(20.5f)  //分数离顶部的间隔大小

        //其他设置
        meter.getDataManager().Rate = 30    //分数弧动画速率（20/s）

        meter.getDataManager().ShowSource = true //是否显示分数

        meter.getDataManager().SourceTextAlign = MeterView.SOURCE_CENTER    //分数居中方式 SOURCE_CENTER（底盘居中） OURCE_START（底盘顶部） SOURCE_END （底盘底部）

	//meter.getDataManager().startAngle = -180f   //底盘弧开始点的角度（起点为组件最右边点，当等于-90时为组件中点）
	//meter.getDataManager().sweepAngle = 180f    //底盘弧角度大小

        meter.getDataManager().sourceShowType = MeterView.SOURCE_NUMBER  //显示的分数类型 SOURCE_NUMBER:数字（百分制） SOURCE_SCALETEXT:刻度文字
        meter.getDataManager().meterDebug = false   //打开底部矩形，方便调试和添加其他元素

        //跑分
        tv.text = "runSource(45)......"
        meter.runSource(45) //根据分数跑分（百分制，大于100则等于100）
	//meter.runIndex(5) //根据数据索引跑分 （不为-1，不大于数据大小）
	//meter.addIndex(2) //在当前基础上增加两个索引的分数
	//meter.subtractIndex(1) //在当前基础上减少一个索引的分数
	//meter.addSource(20)//在当前基础上增加20分
	//meter.subtractSource(20)//在当前基础上减少20分
        thread {            //支持异步线程直接调用
            Thread.sleep(4000)
            meter.addIndex(3)
            runOnUiThread(Runnable { tv.text = "addIndex(3)......" })
            Thread.sleep(4000)
            meter.subtractIndex(1)
            runOnUiThread(Runnable { tv.text = "subtractIndex(1)......" })
            Thread.sleep(4000)
            meter.addSource(20)
            runOnUiThread(Runnable { tv.text = "addSource(20)......" })
            Thread.sleep(4000)
            meter.subtractSource(20)
            runOnUiThread(Runnable { tv.text = "subtractSource(20)......" })
            Thread.sleep(4000)
            meter.runIndex(10)
            runOnUiThread(Runnable { tv.text = "runIndex(10)......" })
        }
        //分数改变监听器
        meter.setSourceChangeListener(object :MeterView.SourceChangeListener{
           override fun CheckScaleTextChange(currentText: String) { //选中刻度文字改变
               Log.e("currentScaleText"," currentScaleText $currentText")
           }

           override fun SourceChange(source: Int,currentScaleText:String) {     //分数改变
               Log.e("meter","source $source currentScaleText $currentScaleText")
           }
       })
