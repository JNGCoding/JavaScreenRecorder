Not being able to execute the jar file ?
1) Update your java files.
	- Install the latest Version of java in your computer.

; There is no flexibility in the code, so the recording will always be in the Current Working Directory as output.mp4.

Doesn't understand what the Audio config is for ?
1) Audio Config is made to identify your Virtual Audio Drivers from which your System Audio will be looped in order to be saved.
2) Absorb Line stores the information about your Virtual Audio OUTPUT.
3) Reflect Line stores the information about your Speaker DataLine where your Absorb line will be linked to.

; In order to record the audio, You would still need to set your audio output to your Virtual Audio INPUT.
; This allows the user to still hear your System Audio all the while recording it in a .wav file.

Doesn't understand what the Video config is for ?
1) Video Config stores the information on about the FPS you need on your recording (If possible).
2) Whether to allow the system audio to be recorded.
3) Since, my Screen recorder is not so optimized to offload the recording task on GPU. It cannot record the entire screen at a stable, high frame-rate and hence You have to configure the Recorder on "How much Area it should capture ?"

; X attribute - It tells the Recorder, where on the screen do you want start your capture in X axis.
; Y attribute - It tells the Recorder, where on the screen do you want start your capture in Y axis.
; W attribute - It is the width of the capture area that the recorder will capture.
; H attribute - It is the height of the capture area that the recorder will capture.

Why are there separate files for System Audio and Video Recording ?
1) It was a skill issue.
2) I was unable to find a library that does video encoding as well as audio encoding.
3) I was unable to synchronize the audio and video.