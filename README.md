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
       
	     implementation 'com.github.pimaryschoolstudent:MeterView:Tag'
	     
	}
