package org.jngcoding.screenrecorder.lib_files.audio_capture;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.jngcoding.screenrecorder.lib_files.wav_writer.WaveFileConfig;
import org.jngcoding.screenrecorder.lib_files.wav_writer.WaveFileWriter;

public class AudioLooper {
    public final String absorbline_name;
    public final String reflectline_name;
    public final AudioFormat absorbline_format;
    public final AudioFormat reflectline_format;
    private TargetDataLine absorbLine;
    private SourceDataLine reflectLine;
    public AtomicBoolean running = new AtomicBoolean(false);
    public final WaveFileWriter wav_writer;

    public AudioLooper(String input_name, String output_name, AudioFormat input_format, AudioFormat output_format) {
        absorbline_name = input_name;
        reflectline_name = output_name;
        absorbline_format = input_format;
        reflectline_format = output_format;

        wav_writer = new WaveFileWriter();
    }

    private void attach() throws LineUnavailableException {
        absorbLine = DriverExtract.getTargetLine(absorbline_name, absorbline_format);
        reflectLine = DriverExtract.getSourceLine(reflectline_name, reflectline_format);

        absorbLine.open();
        absorbLine.start();
        reflectLine.open();
        reflectLine.start();
    }

    private void detach() {
        assert absorbLine instanceof TargetDataLine;
        assert reflectLine instanceof SourceDataLine;

        absorbLine.drain();
        absorbLine.close();
        reflectLine.drain();
        reflectLine.close();
    }

    public void setWavConfig(WaveFileConfig config) {
        wav_writer.setConfig(config);
    }

    // Write to a wav_file as well as reflect everything on the Source Audio Line. 
    public void start(String path) throws IOException, LineUnavailableException {
        running.set(true);
        wav_writer.openFile(path);
        attach();

        Thread LoopThread = new Thread() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public void run() {
                byte[] buffer = new byte[4096];
                while (running.get()) {
                    int bytesread = absorbLine.read(buffer, 0, buffer.length);
                    if (bytesread > 0) {
                        reflectLine.write(buffer, 0, bytesread);
                        try {
                            wav_writer.write(buffer, 0, bytesread);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                detach();
            }
        };

        LoopThread.setDaemon(true);
        LoopThread.start();
    }

    public void stop() throws IOException, InterruptedException {
        running.set(false);
        Thread.sleep(100);
        wav_writer.finishFile();
    }
}
