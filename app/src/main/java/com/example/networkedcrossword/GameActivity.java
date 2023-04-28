package com.example.networkedcrossword;

import static java.lang.Math.min;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

public class GameActivity extends AppCompatActivity {
    CrosswordBoard crosswordBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        LinearLayout layout = new LinearLayout(this);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        layout.setOrientation(LinearLayout.VERTICAL);

        // Variables for placing prompt in right place
        int offset = 20;
        Point navBarSize = getNavigationBarSize(this);
        int heightNavBar = navBarSize.y + offset;



        // Screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowHeight = displayMetrics.heightPixels;
        int windowWidth = displayMetrics.widthPixels;

        // dimensions and offsets for prompt
        int size = min(windowHeight, windowWidth);
        int sizeUpdated = size - offset;
        int heightSizeUpdated = size/3 - offset;

        GridView promptView = findViewById(R.id.promptView);

        promptView.setLayoutParams(new ViewGroup.LayoutParams(sizeUpdated, heightSizeUpdated));

        // Center the promptview at the bottom of the screen
        int promptHeight = windowHeight - heightSizeUpdated - heightNavBar;

//        int promptHeight = windowHeight - heightSizeUpdated;
        promptView.setY(promptHeight);
        promptView.setX(offset/2);



        Data data = (Data) getIntent().getSerializableExtra("data");
        Game game = (Game) getIntent().getSerializableExtra("game");

        TextView whoAmIText = findViewById(R.id.whoAmIText);
        if(game.isServer) {
//            whoAmIText.bringToFront();
            whoAmIText.setText("Player 1");
        }
        else {
//            whoAmIText.bringToFront();
            whoAmIText.setText("Player 2");
        }

        crosswordBoard = new CrosswordBoard(this);
        crosswordBoard.setAttributes(game.isServer ? 1 : 2, game);
        setContentView(crosswordBoard);

//        layout.removeAllViews();
//        layout.addView(crosswordBoard);
//        layout.addView(promptView);
//        setContentView(layout);


//        promptView.bringToFront();
//        promptView.requestLayout();
//        promptView.invalidate();

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

    // Code below from https://stackoverflow.com/questions/36514167/how-to-really-get-the-navigation-bar-height-in-android
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
                size.x = (Integer)     Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            }
            catch (IllegalAccessException e) {}
            catch     (InvocationTargetException e) {}
            catch (NoSuchMethodException e) {}
        }

        return size;
    }



}