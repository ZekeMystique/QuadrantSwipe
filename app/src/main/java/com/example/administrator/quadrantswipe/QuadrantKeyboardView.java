package com.example.administrator.quadrantswipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Rohan Dawson on 19/11/2015.
 */
public class QuadrantKeyboardView extends View {

    Paint paint;
    CharacterTree myCharTree;
    NumberTree myNumTree;

    //Constructor to build instance from XML file
    public QuadrantKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight(), paint);
        /*
        canvas.drawText(myCharTree.outputBuffer.toString(), 0, 0, paint);


        How to access objects from other contexts?

        Keyboard layout and design are created here.

        Swiping through the tree should call this onDraw method and redraw the whole keyboard.

        Need reference to myCharTree to be able to tell
        - the contents of outputBuffer (what has been typed already)
        - the letters to display in each quadrant (i.e. how far down the tree we are)

        From what I have read, it is impossible to have a button (spacebar, delete) inside a view.
        This means we should manually add this functionality using getX() getY() in a on touch
        listener callback.
        */

    }
}
