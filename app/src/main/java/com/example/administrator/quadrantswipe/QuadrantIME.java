package com.example.administrator.quadrantswipe;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

/**
 * Created by Rohan Dawson on 17/11/2015.
 */
public class QuadrantIME extends InputMethodService
       // implements GestureDetector.OnGestureListener
{


    GestureDetector detector;
    public CharacterTree myCharTree;

    //A custom view is needed so that we can attach an onTouchListener
    View quadView;


    /*
    * Text input requires a service, not an activity.
    * This is now our core file
    * */

    //Method gets called when the service starts. A View is 'inflated' for the user interface
    @Override
    public View onCreateInputView() {
        myCharTree = new CharacterTree();
        detector = new GestureDetector(this, new GestureListener());

        //Creates the user interface held in the custom view QuadrantKeyboardView
        quadView = getLayoutInflater().inflate(R.layout.activity_quadrants, null);

        //Attaches a listener to the custom view
        quadView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        return quadView;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if(diffX < 0 && diffY < 0){
                    if(checkSwipe(diffX, diffY, velocityX, velocityY)){
                        onSwipeUpLeft();
                        return true;
                    }
                }
                if(diffX < 0 && diffY > 0){
                    if(checkSwipe(diffX, diffY, velocityX, velocityY)){
                        onSwipeDownLeft();
                        return true;
                    }
                }
                if(diffX > 0 && diffY > 0){
                    if(checkSwipe(diffX, diffY, velocityX, velocityY)){
                        onSwipeDownRight();
                        return true;
                    }
                }
                if(diffX > 0 && diffY < 0){
                    if(checkSwipe(diffX, diffY, velocityX, velocityY)){
                        onSwipeUpRight();
                        return true;
                    }
                }

                result = true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }



    private static final String TAG = "Swipetesting";
    private static final int SWIPE_MIN_DISTANCE = 25;
    private static final int SWIPE_THRESHOLD_VELOCITY = 20;
    private static boolean caps = false;


    public boolean checkSwipe(float diffX, float diffY, float velocityX, float velocityY){
        return (Math.abs(diffX) > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                && (Math.abs(diffY) > SWIPE_MIN_DISTANCE) && (Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY);
    }

    public void onSwipeUpRight(){
        Log.d(TAG, "Swipe Up-Right");
        handleText(myCharTree.onTopRightSwipe());
    }

    public void onSwipeDownRight(){
        Log.d(TAG, "Swipe Down-Right");
        handleText(myCharTree.onBottomRightSwipe());
    }

    public void onSwipeDownLeft(){
        Log.d(TAG, "Swipe Down-Left");
        handleText(myCharTree.onBottomLeftSwipe());
    }

    public void onSwipeUpLeft(){
        Log.d(TAG, "Swipe Up-Left");
        handleText(myCharTree.onTopLeftSwipe());
    }

    public void onShiftClick(View view) {
        if(!caps){
            caps = true;
        }
        else{
            caps = false;
        }
    }

    public void onSpaceClick(View view) {
        handleText(" ");
    }

    public void handleText(String inText){
        InputConnection ic = getCurrentInputConnection();
        if(caps){
            inText = inText.toUpperCase();
        }

        if(inText != null){
            ic.commitText(inText, 1);
            caps = false;
        }
    }



}
