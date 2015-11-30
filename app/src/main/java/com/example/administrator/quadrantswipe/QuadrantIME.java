package com.example.administrator.quadrantswipe;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import android.widget.Button;


/**
 * Created by Rohan Dawson on 17/11/2015.
 */
public class QuadrantIME extends InputMethodService
       // implements GestureDetector.OnGestureListener
{


    GestureDetector detector;
    GestureDetector detectorForNumbers;
    public CharacterTree myCharTree;
    public NumberTree myNumTree;
    public Boolean onNumbersPage = false;
    public Boolean onLettersPage = false;

    //A custom view is needed so that we can attach an onTouchListener
    View quadView;
    View quadViewForNumbers;

    /*
    * Text input requires a service, not an activity.
    * This is now our core file
    * */

    //Method gets called when the service starts. A View is 'inflated' for the user interface
    @Override
    public View onCreateInputView() {
        myCharTree = new CharacterTree();

        onLettersPage = true;
        onNumbersPage = false;

        detector = new GestureDetector(this, new GestureListener());

        //Creates the user interface held in the custom view QuadrantKeyboardView
        if(onLettersPage)
        {
            quadView = getLayoutInflater().inflate(R.layout.activity_quadrants, null);
        }
//        else
//        {
//            quadView = getLayoutInflater().inflate(R.layout.content_numbers, null);
//        }

    // ** Justin Messing around **
//        Button numbersButton = (Button)findViewById(R.id.numbersButton);
//        numbersButton.setOnClickListener(
//                new Button.onClickListener(){
//                    public void onClick(View v){
//                        TextView topLeft = (TextView)findViewById(R.id.topLeft);
//                        topLeft.setText("It Works");
//                    }
//                }
//        );

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
//        handleText(myCharTree.onTopRightSwipe());
        if(onNumbersPage)
        {
            handleText(myNumTree.onTopRightSwipe());
        }
        else
        {
            handleText(myCharTree.onTopRightSwipe());
        }
    }

    public void onSwipeDownRight() {
        Log.d(TAG, "Swipe Down-Right");
//        handleText(myCharTree.onBottomRightSwipe());
        if (onNumbersPage)
        {
            handleText(myNumTree.onBottomRightSwipe());
        }
        else
        {
            handleText(myCharTree.onBottomRightSwipe());
        }

    }

    public void onSwipeDownLeft() {
        Log.d(TAG, "Swipe Down-Left");
//        handleText(myCharTree.onBottomLeftSwipe());
        if (onNumbersPage)
        {
            handleText(myNumTree.onBottomLeftSwipe());
            TextView myText0 = (TextView)quadView.findViewById(R.id.topLeft);
            myText0.setText("7    \n\n     ");
            TextView myText1 = (TextView)quadView.findViewById(R.id.topRight);
            myText1.setText("8    \n\n     ");
            TextView myText2 = (TextView)quadView.findViewById(R.id.botLeft);
            myText2.setText("!    \n\n     ");
            TextView myText3 = (TextView)quadView.findViewById(R.id.botRight);
            myText3.setText("?    \n\n     ");
        }
        else
        {
            handleText(myCharTree.onBottomLeftSwipe());
        }
    }

    public void onSwipeUpLeft() {
        Log.d(TAG, "Swipe Up-Left");
//        handleText(myCharTree.onTopLeftSwipe());
        if (onNumbersPage)
        {
            handleText(myNumTree.onTopLeftSwipe());


        }
        else
        {
            handleText(myCharTree.onTopLeftSwipe());
        }
    }

    public void onShiftClick(View view) {
        if (!caps) {
            caps = true;
        } else {
            caps = false;
        }
    }

    public void onSpaceClick(View view) {
        handleText(" ");
    }

    public void onDelClick(View view) {
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


    public void onNumbersClick(View view) {
        Log.d(TAG, "Clicking to go to Numbers");

        myNumTree = new NumberTree();

        onNumbersPage = true;
        onLettersPage = false;



        Button myButton = (Button)quadView.findViewById(R.id.numbersButton);
        myButton.setText("letters");



        TextView myText0 = (TextView)quadView.findViewById(R.id.topLeft);
        myText0.setText("1   2\n\n3   .");
        TextView myText1 = (TextView)quadView.findViewById(R.id.topRight);
        myText1.setText("4   5\n\n6   ,");
        TextView myText2 = (TextView)quadView.findViewById(R.id.botLeft);
        myText2.setText("7   8\n\n!   ?");
        TextView myText3 = (TextView)quadView.findViewById(R.id.botRight);
        myText3.setText("9   0\n\n@   #");



        if(onNumbersPage)
        {
            onNumbersPage = false;
            onLettersPage = true;
            myButton.setText("letters");
        }
        if(!onNumbersPage)
        {
            onNumbersPage = true;
            onLettersPage = false;
            myButton.setText("numbers");
        }


    }
    public void onLettersClick(View view) {
        Log.d(TAG, "Clicking to go to Letters");

        onNumbersPage = false;
        onLettersPage = true;

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
    }
    public void updatePreview(){
        InputConnection ic = getCurrentInputConnection();
        ExtractedTextRequest myReq = new ExtractedTextRequest();
        //ic.getExtractedText(myReq, 0);
        TextView outputText = (TextView) quadView.findViewById(R.id.outputText);
        outputText.setText(ic.getExtractedText(myReq, 0).text);
    }
}
