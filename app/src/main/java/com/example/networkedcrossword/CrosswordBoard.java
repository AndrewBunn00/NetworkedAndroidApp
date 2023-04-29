package com.example.networkedcrossword;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

public class CrosswordBoard extends View{
    private int player;
    private Game game;
    private Paint paint = new Paint();
    private int originX = 0;
    private int originY = 0;
    private int maxY = 0;
    private int maxX = 0;
    private String[][] board;
    public CrosswordBoard(Context context) {
        super(context);
    }

    public void setAttributes(int player, Game game, String[][] newBoard) {
        this.player = player;
        this.game = game;
//        this.board = game.getBoard();
        this.board = newBoard;
        invalidate(); // force view to redraw itself
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Set spatial dimensions
        // Basically padding to prevent running off screen
        this.maxX = canvas.getWidth()-100;
        this.maxY = canvas.getHeight() - 50;
        this.originX = 100;
        this.originY = canvas.getHeight()/16;
        int distanceX = maxX - originX;
        int distanceY = maxY - originY;
        int cellSize = distanceX / this.board.length;

        // Set general style sizings
        int H1 = 70;
        int body = 50;
        // Draw here
        paint.setColor(Color.BLACK);
        StringBuilder sb = new StringBuilder();
        if(game.turn == this.player) {
            paint.setTextSize(H1);
            sb.append("Your Turn");
            canvas.drawText(sb.toString(),distanceX/2-70, originY, paint);
        }

        // Reset string builder and set a new string
        sb.setLength(0);
        sb.append("Player: ");
        sb.append(this.player);
        String player = sb.toString();


        // Resize text
        paint.setTextSize(body);
        if(game.turn == this.player) {
            canvas.drawText(player, distanceX/2, originY+H1+20, paint);
            // Move game board cursor down
            this.originY = originY + H1+120;
        } else {
            canvas.drawText(player, distanceX/2, originY, paint);
            // Move game board cursor down
            this.originY = originY + 100;
        }

        // Create 3 paint instances
        Paint emptyCell = new Paint();
        emptyCell.setColor(Color.BLACK);
        emptyCell.setStyle(Paint.Style.STROKE);
        emptyCell.setStrokeWidth(2);

        Paint filledCell = new Paint();
        filledCell.setColor(Color.BLACK);

        Paint testCell = new Paint();
        testCell.setColor(Color.BLUE);

        // Build out the grid
        String fillChar = "*";
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j <this.board[0].length; j++) {
                Rect rect = new Rect(originX + (i * cellSize), originY + (j * cellSize), originX+((i+1)*cellSize), originY+((j+1)*cellSize));
                if(this.board[i][j].equals(fillChar)){
                    canvas.drawRect(rect, filledCell);
                } else if(this.board[i][j].equals("-")){
                    canvas.drawRect(rect, emptyCell);
                } else {
                    canvas.drawRect(rect, emptyCell);
                    canvas.drawText(this.board[i][j], originX + (i*cellSize), originY+(j*cellSize)+body, paint);
                }
            }
        }
    }

}