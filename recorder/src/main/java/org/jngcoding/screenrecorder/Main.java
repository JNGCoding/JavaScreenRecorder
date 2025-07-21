package org.jngcoding.screenrecorder;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jngcoding.screenrecorder.gui.ControlProgram;
import org.jngcoding.screenrecorder.gui.RoundButton;
import org.jngcoding.screenrecorder.lib_files.Capture;
import org.jngcoding.screenrecorder.lib_files.audio_capture.AudioLooper;
import org.jngcoding.screenrecorder.lib_files.video_io.VideoWriter;
import org.jngcoding.screenrecorder.lib_files.wav_writer.WaveFileConfig;

import com.github.cliftonlabs.json_simple.JsonException;

public class Main {
    // Path definitions
    private static final String CWD = System.getProperty("user.dir");
    private static final String AUDIO_CONFIG_PATH = CWD + "\\AudioConfig.json";
    private static final String VIDEO_CONFIG_PATH = CWD + "\\VideoConfig.json";

    // GUI
    private static final ControlProgram program = new ControlProgram("Control Center", 300, 250);
    private static final VideoWriter video_writer = new VideoWriter();
    private static AudioLooper audio_looper = null;

    // Loading the configs
    private static AudioConfig audio_config;
    private static VideoConfig video_config;

    static {
        try {
            audio_config = new AudioConfig(AUDIO_CONFIG_PATH);
            video_config = new VideoConfig(VIDEO_CONFIG_PATH);
        } catch (JsonException e) {
            System.out.println("Error message : " + e.getMessage());
        }
    }

    // Thread Flags
    private static boolean RecordFlag = false;

    private static void PrintCrash(Exception exception) {
        System.out.println("Error Name : " + exception.getClass().getName());
        System.out.println("Error Message : " + exception.getMessage());
        System.out.println("Error Cause : " + exception.getCause().toString());
        System.out.println("Stack Trace :\n" + Arrays.toString(exception.getStackTrace()));
    }

    private static void reportCrash(Exception exception) {
        JOptionPane.showMessageDialog(null, exception.toString());
        PrintCrash(exception);
        System.exit(0);
    }

    private static void configureCommands() {
        // Start Recording
        ((RoundButton) program.components.get("StartRecording")).addActionListener((e) -> {
            if (e.getSource() == program.components.get("StartRecording")) {
                ((JFileChooser) program.components.get("FileChooser")).showSaveDialog(program.panel);
                String path = ((JFileChooser) program.components.get("FileChooser")).getSelectedFile().getAbsolutePath();
                video_writer.open(path + ".mp4");

                RecordFlag = true;
                try {
                    video_writer.check_and_create(); 
                    video_writer.start_encoder(video_config.getFPS());
                    
                    if (video_config.getSystemAudio() && audio_looper != null) {
                        try {
                            audio_looper.start(path + ".wav"); 
                        } catch (LineUnavailableException | IOException exception) {
                            reportCrash(exception);
                        }
                    }

                    Thread Rec = new Thread(() -> {
                        while (RecordFlag) {
                            try {
                                BufferedImage image = Capture.capture();
                                long SystemTime = System.currentTimeMillis();
                                while (System.currentTimeMillis() - SystemTime < video_config.getFrameTime()) {
                                    video_writer.encode_image(image);
                                }
                            } catch (AWTException | IOException exception) {
                                reportCrash(exception);
                            }
                        }
                    }); Rec.setDaemon(true); Rec.start();
                } catch (IOException io_exception) {
                    reportCrash(io_exception);
                }
            }
        });

        // Stop Recording
        ((RoundButton) program.components.get("StopRecording")).addActionListener((e) -> {
            if (e.getSource() == program.components.get("StopRecording")) {
                RecordFlag = false;
                try {
                    Thread.sleep(100); // Just a safety step (to wait for the RecThread to close on its own.)
                    if (video_config.getSystemAudio() && audio_looper != null) {
                        try {
                            audio_looper.stop();
                        } catch (IOException | InterruptedException exception) {
                            reportCrash(exception);
                        }
                    }
                    video_writer.stop_encoder();
                } catch (IOException | InterruptedException exception) {
                    reportCrash(exception);
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        String Microphone = (String) audio_config.TargetAudioConfig.get("Name");
        String Speaker = (String) audio_config.SourceAudioConfig.get("Name");
        AudioFormat MicroFormat = audio_config.getAudioFormatOfAbsorbLine();
        AudioFormat SpeakFormat = audio_config.getAudioFormatOfReflectLine();

        audio_looper = new AudioLooper(Microphone, Speaker, MicroFormat, SpeakFormat);
        audio_looper.setWavConfig(new WaveFileConfig(16, 1, 1, 44100, 16));

        configureCommands();
        program.setVisible(true);
    }
}