package org.jngcoding.screenrecorder.gui;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;

public class ControlProgram {
    public final GUIWindow screen;
    public final GUIPanel panel;
    public final HashMap<String, Component> components = new HashMap<>();

    public ControlProgram(String title, int width, int height) {
        screen = new GUIWindow(title, width, height);
        panel = new GUIPanel();

        autoload();
    }

    public ControlProgram(String title, int width, int height, int x, int y) {
        screen = new GUIWindow(title, width, height, x, y);
        panel = new GUIPanel();

        autoload();
    }

    private void autoload() {
        screen.add(panel);

        /*
         * Button1 (Start Recording) - Starts a new thread that records every thing present on the screen.
         * Button2 (Stop Recording) - Stops the recording thread and saves the file.
         * FileChooser - Loads the file
        */

        RoundButton start_recording = new RoundButton("Start Recording");
        start_recording.setBounds(50, 0, 200, 100);

        RoundButton stop_recording = new RoundButton("Stop Recording");
        stop_recording.setBounds(50, 110, 200, 100);

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        components.put("StartRecording", start_recording);
        components.put("StopRecording", stop_recording);
        components.put("FileChooser", chooser);

        for (int i = 0; i < components.size(); i++) {
            panel.add((Component) components.values().toArray()[i]);
        }
    }

    public void setVisible(boolean c) {
        screen.setVisible(true);
    }
}
