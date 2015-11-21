# QuadrantSwipe

Status:
- Keyboard activity fully functional
- Keyboard service registers gestures and successfully sends input back to OS. No user interface yet though

To do:
- Write QuadrantKeyboardView's onDraw() method. Need to figure out how to access other objects from the current context
- Need to figure out how implement buttons (space bar, delete) into the View. Details in file. 
- Shuffle around CharacterTree based on our observations from usage
- App lifecycle - onPause(), onResume(), onDestroy() etc. 
