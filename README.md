# Kotlin Multi Option Switch Library

#### This is a open source Multi Option Switch View.
[![](https://jitpack.io/v/yeocak/KotlinMultiOptionSwitch.svg)](https://jitpack.io/#yeocak/KotlinMultiOptionSwitch) <br>
![](https://raw.githubusercontent.com/yeocak/KotlinMultiOptionSwitch/master/forgithub/app.gif)

<br>

## Add to your layout: 

    <com.yeocak.kotlinmultioptionswitchlib.MultiOptionSwitch
        android:id="@+id/multiOptionSwitch"
        android:layout_width="match_parent"
        android:layout_height="40dp"/>
        
Layout customizing:

        app:option_count = INT -> You should define how many option you want. DEFAULT = 3
        app:default_selected_option = INT -> You can change the starter option if you want. DEFAULT = 1
        app:background_visible = BOOLEAN -> Background visibility. DEFAULT = true
        app:shadow_visible = BOOLEAN -> Shadow visibility. DEFAULT = true
        app:background_color = COLOR -> Background color. DEFAULT = "#61707D"
        app:selector_color = COLOR -> Selector ball color. DEFAULT = "#FFFFFF"

<br>

## Usage:

    val multiOptionSwitch = findViewById<MultiOptionSwitch>(R.id.multiOptionSwitch)
    
    multiOptionSwitch.setOptionChangedListener { 
        // Triggers everytime user changes option
        // DO SOMETHING
    }
    
    multiOptionSwitch.selectOption(2) // You can select an option anytime
    
<br>

## For adding to your project:

Add it in your root build.gradle at the end of repositories:

    allprojects {
    	repositories {
    		...
    		maven { url 'https://jitpack.io' }
    	}
    }
    
Add the dependency:

    dependencies {
      implementation 'com.github.yeocak:KotlinMultiOptionSwitch:0.2.0'
    }
