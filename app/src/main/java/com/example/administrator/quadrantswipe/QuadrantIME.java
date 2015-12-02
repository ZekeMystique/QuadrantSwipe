package com.example.administrator.quadrantswipe;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Rohan Dawson on 17/11/2015.
 */
public class QuadrantIME extends InputMethodService
       // implements GestureDetector.OnGestureListener
{


    GestureDetector detector;
    public CharacterTree charMap;
    public CharacterTree numMap;
    public CharacterTree myCharTree;
    public boolean usingNums;
    //public LayoutManager myLayman;


    //A custom view is needed so that we can attach an onTouchListener
    View quadView;



    /*
    * Text input requires a service, not an activity.
    * This is now our core file
    * */

    //Method gets called when the service starts. A View is 'inflated' for the user interface
    @Override
    public View onCreateInputView() {
        charMap = new CharacterTree(true);
        numMap = new CharacterTree(false);
        myCharTree = charMap;
        usingNums = false;
        detector = new GestureDetector(this, new GestureListener());

        //Creates the user interface held in the custom view QuadrantKeyboardView
        quadView = getLayoutInflater().inflate(R.layout.activity_quadrants, null);
       adjustView();

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

                if (diffX < 0 && diffY < 0) {
                    if (checkSwipe(diffX, diffY, velocityX, velocityY)) {
                        onSwipeUpLeft();
                        return true;
                    }
                }
                if (diffX < 0 && diffY > 0) {
                    if (checkSwipe(diffX, diffY, velocityX, velocityY)) {
                        onSwipeDownLeft();
                        return true;
                    }
                }
                if (diffX > 0 && diffY > 0) {
                    if (checkSwipe(diffX, diffY, velocityX, velocityY)) {
                        onSwipeDownRight();
                        return true;
                    }
                }
                if (diffX > 0 && diffY < 0) {
                    if (checkSwipe(diffX, diffY, velocityX, velocityY)) {
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


    public boolean checkSwipe(float diffX, float diffY, float velocityX, float velocityY) {
        return (Math.abs(diffX) > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                && (Math.abs(diffY) > SWIPE_MIN_DISTANCE) && (Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY);
    }

    public void onSwipeUpRight() {
        Log.d(TAG, "Swipe Up-Right");
        handleText(myCharTree.onTopRightSwipe());
    }

    public void onSwipeDownRight() {
        Log.d(TAG, "Swipe Down-Right");
        handleText(myCharTree.onBottomRightSwipe());
    }

    public void onSwipeDownLeft() {
        Log.d(TAG, "Swipe Down-Left");
        handleText(myCharTree.onBottomLeftSwipe());
    }

    public void onSwipeUpLeft() {
        Log.d(TAG, "Swipe Up-Left");
        handleText(myCharTree.onTopLeftSwipe());
    }

    public void onShiftClick(View view) {
        if (!caps) {
            caps = true;
        } else {
            caps = false;
        }
        adjustView();
    }
    public void onNumClick(View view) {
        myCharTree.pointer = myCharTree.root;
        Button myButton = (Button)quadView.findViewById(R.id.numToggle);
        if(usingNums){
            myCharTree = charMap;
            usingNums = false;
            myButton.setText("123");
        }
        else
        {
            myCharTree = numMap;
            usingNums = true;
            myButton.setText("ABC");
        }

        adjustView();
    }

    public void onSpaceClick(View view) {
        handleText(" ");
    }

    public void onDelClick(View view) {
        if(myCharTree.pointer != myCharTree.root) {
            myCharTree.pointer = myCharTree.root;
            adjustView();
        }
        Log.d(TAG, "Trying to Delete");
        InputConnection ic = getCurrentInputConnection();
        ic.deleteSurroundingText(1, 0);
        updatePreview();
    }

    public void onEnterClick(View view) {


        InputConnection ic = getCurrentInputConnection();
        KeyEvent myKey = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);
        ic.sendKeyEvent(myKey);
    }


    public void handleText(String inText) {
        InputConnection ic = getCurrentInputConnection();
        if (caps) {
            inText = inText.toUpperCase();
        }

        if (inText != null) {
            ic.commitText(inText, 1);
            caps = false;
        }
        updatePreview();
        adjustView();
    }

    private String padText(String inText) {
        inText = "" +inText+ " \n     ";
        return inText;
    }

    public void updatePreview(){
        InputConnection ic = getCurrentInputConnection();
        ExtractedTextRequest myReq = new ExtractedTextRequest();
        //ic.getExtractedText(myReq, 0);
        TextView outputText = (TextView) quadView.findViewById(R.id.outputText);
        outputText.setText(ic.getExtractedText(myReq, 0).text);
    }

        public void adjustView()
        {
            if(!myCharTree.pointer.bottomRight.leaf){
                setSubMenu();
            }
            else if(myCharTree.getLeaf() == false) {
                    String myString = myCharTree.pointer.topRight.data;
                    setTopRight(padText(myString));
                    myString = myCharTree.pointer.bottomRight.data;
                    setBottomRight(padText(myString));
                    myString = myCharTree.pointer.bottomLeft.data;
                    setBottomLeft(padText(myString));
                    myString = myCharTree.pointer.topLeft.data;
                    setTopLeft(padText(myString));
                }
            }


        public void setSubMenu(){
//            String myString = myCharTree.pointer.topLeft.topLeft.data + " | "
//                    + myCharTree.pointer.topLeft.topRight.data + "\n" + "-   -" + "\n"
//                    + myCharTree.pointer.topLeft.bottomLeft.data + " | "
//                    + myCharTree.pointer.topLeft.bottomRight.data;
//            setTopLeft(myString);
            String myString = myCharTree.pointer.topLeft.topLeft.data + "   "
                    + myCharTree.pointer.topLeft.topRight.data + "\n" + "     " + "\n"
                    + myCharTree.pointer.topLeft.bottomLeft.data + "   "
                    + myCharTree.pointer.topLeft.bottomRight.data;
            setTopLeft(myString);
            myString = myCharTree.pointer.topRight.topLeft.data + "   "
                    + myCharTree.pointer.topRight.topRight.data + "\n" + "     " + "\n"
                    + myCharTree.pointer.topRight.bottomLeft.data + "   "
                    + myCharTree.pointer.topRight.bottomRight.data;
            setTopRight(myString);

            myString = myCharTree.pointer.bottomLeft.topLeft.data + "   "
                    + myCharTree.pointer.bottomLeft.topRight.data + "\n" + "     " + "\n"
                    + myCharTree.pointer.bottomLeft.bottomLeft.data + "   "
                    + myCharTree.pointer.bottomLeft.bottomRight.data;
            setBottomLeft(myString);
            if(myCharTree.pointer != myCharTree.root || usingNums){
            myString = myCharTree.pointer.bottomRight.topLeft.data + "   "
                    + myCharTree.pointer.bottomRight.topRight.data + "\n" + "    " + "\n"
                    + myCharTree.pointer.bottomRight.bottomLeft.data + "   "
                    + myCharTree.pointer.bottomRight.bottomRight.data;
            setBottomRight(myString);}
            else{
                myString = "\n\nMore";      //getString(R.string.menu_br);
                setBottomRight(myString);
            }
        }

//        public void setRootMenu()
//        {
//            String myString = getString(R.string.menu_tr);
//            setTopRight(myString);
//            myString = getString(R.string.menu_br);
//            setBottomRight(myString);
//            myString = getString(R.string.menu_bl);
//            setBottomLeft(myString);
//            myString = getString(R.string.menu_tl);
//            setTopLeft(myString);
//        }

        public void setTopLeft(String s){
            TextView newView = (TextView) quadView.findViewById(R.id.topLeft);
            if(caps){
                newView.setText(s.toUpperCase());
            }
            else{
                newView.setText(s);
            }
        }
        public void setTopRight(String s){
            TextView newView = (TextView) quadView.findViewById(R.id.topRight);
            if(caps){
                newView.setText(s.toUpperCase());
            }
            else{
                newView.setText(s);
            }

        }
        public void setBottomLeft(String s){
            TextView newView = (TextView) quadView.findViewById(R.id.botLeft);
            if(caps){
                newView.setText(s.toUpperCase());
            }
            else{
                newView.setText(s);
            }
        }
        public void setBottomRight(String s){
            TextView newView = (TextView) quadView.findViewById(R.id.botRight);
            if(caps){
                newView.setText(s.toUpperCase());
            }
            else{
                newView.setText(s);
            }
        }
    }



