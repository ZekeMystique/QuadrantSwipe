package com.example.administrator.quadrantswipe;

        import android.gesture.GestureOverlayView;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.GestureDetector;
        import android.view.GestureDetector.OnGestureListener;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.TextView;

public class QuadrantKeyboard extends AppCompatActivity implements GestureDetector.OnGestureListener {
    GestureDetector detector;

    //This activity is now irrelevant.
    //We can potentially add an activity for Settings if we have extra time


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadrants);
        detector = new GestureDetector(this, this);
        //View myView = new View(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    TextView outputText = (TextView) findViewById(R.id.outputText);
    outputText.setText("");
    }
    private CharacterTree myCharTree = new CharacterTree();
    //private static int page = 1;
    private static final String TAG = "Swipetesting";
    private static final int SWIPE_MIN_DISTANCE = 25;
    private static final int SWIPE_THRESHOLD_VELOCITY = 20;
    private static boolean doUpper = false;

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.d(TAG, "+ onTouchEvent(event:" + event + ")");
        detector.onTouchEvent(event);
        Log.d(TAG, "- onTouchEvent()");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            /*if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (diffX > 0) {
                       // onSwipeRight();
                    } else {
                        //onSwipeLeft();
                    }
                }
                result = true;
            }*/
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

    public void handleText(String inText){
        if(doUpper){
            inText = inText.toUpperCase();
        }
        TextView outputText = (TextView) findViewById(R.id.outputText);
        if(inText != null){

            outputText.append(inText);
            doUpper = false;
        }
    }

    public void onDelClick(View view) {
        Log.d(TAG, "Trying to Delete");
        TextView outputText = (TextView) findViewById(R.id.outputText);
        if(myCharTree.pointer != myCharTree.root){
            myCharTree.pointer = myCharTree.root;
        }
        else if(outputText.getText().length() != 0) {
            String oldText = outputText.getText().toString();
            outputText.setText(oldText.substring(0, oldText.length() - 1));
            Log.d(TAG, "made into a string");
        }
    }

    public void onSpaceClick(View view) {
        handleText(" ");
    }

    public void onShiftClick(View view) {
        if(!doUpper){
            doUpper = true;
        }
        else{
            doUpper = false;
        }
    }

    public void populateView(CharacterTree tree){
        if(tree.pointer != tree.root);
        {

        }

    }
}
