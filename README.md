# Kotlin Multi Option Switch Library

#### This is a open source Multi Option Switch View. You can change "Option Count", "Default Selected Option".
![](https://raw.githubusercontent.com/yeocak/KotlinMultiOptionSwitch/master/forgithub/app.gif)

<br>
Add to your layout:

    <com.yeocak.kotlinmultioptionswitchlib.MultiOptionSwitch
        android:id="@+id/multiOptionSwitch"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        app:option_count="6"
        app:default_selected_option="3"/>

<br>
You can use listener for user interaction:

    val multiOptionSwitch = findViewById<MultiOptionSwitch>(R.id.multiOptionSwitch)
    
    multiOptionSwitch.setOptionChangedListener {
      // DO SOMETHING
    }
    
<br>
You an choose a option manually:

    multiOptionSwitch.selectOption(2)
    

-----------
### For adding to your project:

Add it in your root build.gradle at the end of repositories:

    allprojects {
    	repositories {
    		...
    		maven { url 'https://jitpack.io' }
    	}
    }
    
Add the dependency:

    dependencies {
      implementation 'com.github.yeocak:KotlinMultiOptionSwitch:0.1.0'
    }
