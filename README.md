# QCLAutoCycleView
一行代码快速实现今日头条 网易新闻焦点图自动循环轮播效果

##实现功能
，1，自动循环轮播，可以设置时间
，2，可以手动实现循环滑动
，3，可以自定义轮播时圆点大小和颜色

##老规矩先看效果图
![image](https://github.com/qiushi123/QCLAutoCycleView/blob/master/images/GIF.gif?raw=true)

##一行代码快速使用
```xml
<?xml version="1.0" encoding="utf-8"?>  
<RelativeLayout  
    xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    >  
  
  
    <com.qclautocycleview.AutoCycleView  
        android:id="@+id/cycle_view"  
        android:layout_width="match_parent"  
        android:layout_height="300dp"/>  
</RelativeLayout>  
```

```java
public class MainActivity extends AppCompatActivity {  
    public AutoCycleView cycleView;  
  
  
    List<String> mViewList = new ArrayList<String>();//顶部用于循环的布局集合  
  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        cycleView = (AutoCycleView) findViewById(R.id.cycle_view);  
        initCycleViewPager();  
        cycleView.setViewList(mViewList, this);  
        cycleView.startCycle();//开始自动滑动  
    }  

    public void initCycleViewPager() {  
        mViewList = new ArrayList<String>();  
        mViewList.add("第五页");  
        mViewList.add("第一页");  
        mViewList.add("第二页");  
        mViewList.add("第三页");  
        mViewList.add("第四页");  
        mViewList.add("第五页");  
        mViewList.add("第一页");  
    }  
}  
 
```
##实现原理可以移步到博客详解
http://blog.csdn.net/qiushi_1990/article/details/54907387
