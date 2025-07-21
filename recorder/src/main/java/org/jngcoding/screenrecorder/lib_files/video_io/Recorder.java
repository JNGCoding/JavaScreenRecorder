package org.jngcoding.screenrecorder.lib_files.video_io;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jngcoding.screenrecorder.VideoConfig;
import org.jngcoding.screenrecorder.lib_files.Capture;

public class Recorder {
    public final FrameBuffer buffer;
    public ExecutorService executor = null;
    public AWTSequenceEncoder encoder = null;
    public final Robot robot;

    public Runnable captureTask;
    public Runnable encodeTask;

    private AtomicBoolean capturing = new AtomicBoolean(false);
    public VideoConfig _config = null;
    private final int n_threads;

    public Recorder(int allocation_size, int nThreads, VideoConfig config) throws AWTException {
        buffer = new FrameBuffer(allocation_size);
        robot = new Robot();
        _config = config;
        n_threads = nThreads;

        captureTask = () -> {
            long capture_time = System.currentTimeMillis();
            while (capturing.get()) {
                try {
                    while (System.currentTimeMillis() - capture_time < _config.getFrameTime()) {}
                    buffer.put(Capture.capture()); 
                    capture_time = System.currentTimeMillis();
                } catch (InterruptedException | AWTException exception) {
                    System.out.println("Exception in Thread : " + Thread.currentThread().getName() + ", Message : " + exception.getMessage());
                }
            }
        };

        encodeTask = () -> {
            while ((capturing.get() || !buffer.isEmpty()) && encoder != null) {
                try {
                    encoder.encodeImage(buffer.poll(500, TimeUnit.MILLISECONDS));
                } catch (IOException | InterruptedException exception) {
                    System.out.println("Exception in Thread : " + Thread.currentThread().getName() + ", Message : " + exception.getMessage());                    
                }
            }
        };
    }

    public void start(String path) throws IOException {
        executor = Executors.newFixedThreadPool(n_threads);
        encoder = AWTSequenceEncoder.createSequenceEncoder(new File(path), _config.FPS);

        executor.submit(captureTask);
        executor.submit(encodeTask); 
    }

    public void stop() throws IOException, InterruptedException {
        encoder.finish();
        encoder = null;

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        executor = null;
    }
}
