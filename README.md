# Croller
A circular seekbar for Android, with a control knob! (for the lack of a better  word).

# Usage
The library is currently not uploaded to jCenter, so to use it, follow these steps
### build.gradle of Project
```
allprojects {
      repositories {
          jcenter()
          maven{
              url 'https://dl.bintray.com/sdsmdg/Croller/'
          }
     }
}
```
### build.gradle of App
```
compile 'com.sdsmdg.harjot:croller:1.0.0'
```

## in XML
```
<com.sdsmdg.harjot.crollerTest.Croller
        android:id="@+id/croller"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerInParent="true"
        app:back_circle_color="#EDEDED"
        app:indicator_color="#0B3C49"
        app:indicator_width="10"
        app:is_continuous="true"
        app:label_color="#000000"
        app:main_circle_color="#FFFFFF"
        app:max="1000"
        app:progress_primary_color="#0B3C49"
        app:progress_secondary_color="#EEEEEE"
        app:start_offset="45" />
```

## in Java
```
Croller croller = (Croller) findViewById(R.id.croller);
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
        croller.setMainCircleColor(Color.WHITE);
        croller.setMax(50);
        croller.setStartOffset(45);
        croller.setIsContinuous(false);
        croller.setLabelColor(Color.BLACK);
        croller.setProgressPrimaryColor(Color.parseColor("#0B3C49"));
        croller.setIndicatorColor(Color.parseColor("#0B3C49"));
        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
```

# Attributes
XML Attribute | Java set method | Functionality
------------ | ------------- | ------------- 
progress | setProgress(int progress) | Set the current progress of the seekbar
label | setLabel(String str) | Set the label
label_size | setLabelSize(int size) | Set the label size
label_color | setLabelColor(int color) | Set the label color
is_continuous | setIsContinuous(boolean bool) | Set whether seekbar is conitnuous or discrete
max | setMax(int max) | Set the maximum value of the seekbar
start_offset | setStartOffset(int offset) | Set the seekbar start offset angle from bottom horizontal center
sweep_angle | setSweepAngle(int angle) | Set the total angle covered by the seekbar
progress_primary_stroke_width | setProgressPrimaryStrokeWidth(float width) | Set the primary progress thickness for continuous type
progress_secondary_stroke_width | setProgressSecondaryStrokeWidth(float width) | Set the secondary progress thickness for continuous type
progress_primary_circle_size | setProgressPrimaryCircleSize(float size) | Set the primary progress circle size for discrete type
progress_secondary_circle_size | setProgressSecondaryCircleSize(float size) | Set the secondary progress circle size for discrete type
indicator_width  | setIndicatorWidth(float width) | Set the progress indicator width
indicator_color | setIndicatorColor(int color) | Set the progress indicator color
progress_primary_color | setProgressPrimaryColor(int color) | Set the progress primary(active) color
progress_secondary_color | setProgressSecondaryColor(int color) | Set the progress secondary(inactive) color
progress_radius | setProgressRadius(float radius) | Set the radius of the progress arc
main_circle_radius | setMainCircleRadius(float radius) | Set the main(front) circle radius
back_circle_radius | setBackCircleRadius(float radius) | Set the back circle radius
main_circle_color | setMainCircleColor(int color) | Set the main(front) circle color
back_circle_color | setBackCircleColor(int color) | Set the back circle color

# In Action
<img src = "https://github.com/harjot-oberai/Croller/blob/master/gifs/Croller1.gif" ><br>
<img src = "https://github.com/harjot-oberai/Croller/blob/master/gifs/Croller2.gif" ><br>
<img src = "https://github.com/harjot-oberai/Croller/blob/master/gifs/Croller3.gif" >

