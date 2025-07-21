package org.jngcoding.screenrecorder.lib_files.audio_capture;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class DriverExtract {
    public static SourceDataLine getSourceLine(String name, AudioFormat format) throws LineUnavailableException {
        Mixer.Info[] mixer_infos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixer_info : mixer_infos) {
            if (mixer_info.getName().contains(name)) {
                Mixer mixer = AudioSystem.getMixer(mixer_info);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                if (mixer.isLineSupported(info)) {
                    SourceDataLine result = (SourceDataLine) mixer.getLine(info);
                    return result;
                } else {
                    throw new LineUnavailableException("Line Doesn't Support Audio Format.");
                }
            }
        }
        throw new LineUnavailableException("Line does not exist.");
    }

    public static TargetDataLine getTargetLine(String name, AudioFormat format) throws LineUnavailableException {
        Mixer.Info[] mixer_infos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixer_info : mixer_infos) {
            if (mixer_info.getName().contains(name)) {
                Mixer mixer = AudioSystem.getMixer(mixer_info);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (mixer.isLineSupported(info)) {
                    TargetDataLine result = (TargetDataLine) mixer.getLine(info);
                    return result;
                } else {
                    throw new LineUnavailableException("Line Doesn't Support Audio Format.");
                }
            }
        }
        throw new LineUnavailableException("Line does not exist.");
    }

    public static Mixer.Info[] ListAllDrivers() {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] sourceLines = mixer.getSourceLineInfo(); // Output lines
            Line.Info[] targetLines = mixer.getTargetLineInfo(); // Input lines

            System.out.println("Mixer: " + mixerInfo.getName());
            System.out.println("  Description: " + mixerInfo.getDescription());

            System.out.println("  Output Lines:");
            for (Line.Info info : sourceLines) {
                System.out.println("    " + info.toString());
            }

            System.out.println("  Input Lines:");
            for (Line.Info info : targetLines) {
                System.out.println("    " + info.toString());
            }

            System.out.println();
        }

        return mixers;
    }
}
