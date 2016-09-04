package com.example.bob.navbartest;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    View rootView;
    View contentView;

    Handler mHandler;

    Button button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView=findViewById(R.id.rootView);
        contentView=findViewById(R.id.contentView);

        mHandler=new Handler();

        button3=(Button) findViewById(R.id.testButton3);



        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            Log.i("UICHANGE","visible");
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideNavBar();
                                }
                            },4000);
                            //button3.setVisibility(View.INVISIBLE);

                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                            Log.i("UICHANGE","NOT visible");
                            //button3.setVisibility(View.VISIBLE);
                        }
                    }
                });




    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("UICHANGE","dispatchTouchEvent");
        //Check the event and do magic here, such as...
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        }

        //Be careful not to override the return unless necessary
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        Log.i("UICHANGE","onUserInteraction");
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.i("UICHANGE","onTouchEvent");
        return super.onTouchEvent(event);
    }

    public void onTest3Click(View view) {

        Toast.makeText(this,"onTest3Click",Toast.LENGTH_SHORT).show();
        Log.i("UICHANGE","onTest3Click");

        //int navHeight=getNavBarHeight(this);
        Point point=getNavigationBarSize(this);
        Toast.makeText(this,"navbarSize "+point.x+" "+point.y,Toast.LENGTH_SHORT).show();
        //Log.i("UICHANGE","navHeight "+navHeight);

    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        }

        return size;
    }

    public void onTest1Click(View view) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();




        hideNavBar();

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i("UICHANGE","dispatchKeyEvent");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        Log.i("UICHANGE","dispatchKeyShortcutEvent");
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        Log.i("UICHANGE","dispatchGenericMotionEvent");
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        Log.i("UICHANGE","dispatchTrackballEvent");
        return super.dispatchTrackballEvent(ev);
    }



    private void hideNavBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


                ;

        decorView.setSystemUiVisibility(uiOptions);
    }

    public void onTest2Click(View view) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().show();

        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(0);


    }

    public int getNavBarHeight(Context c) {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if(!hasMenuKey && !hasBackKey) {
            //The device has a navigation bar
            Resources resources = getResources();

            int orientation = getResources().getConfiguration().orientation;
            int resourceId;
            if (isTablet(c)){
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
            }  else {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            }

            if (resourceId > 0) {
                return getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    private boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


}
