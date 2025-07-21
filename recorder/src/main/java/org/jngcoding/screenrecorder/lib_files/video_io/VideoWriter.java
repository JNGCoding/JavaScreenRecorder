package org.jngcoding.screenrecorder.lib_files.video_io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jcodec.api.awt.AWTSequenceEncoder;

public class VideoWriter {
    private File video_file = null;
    private AWTSequenceEncoder encoder = null;

    public VideoWriter(File file) { video_file = file; }
    public VideoWriter(String path) { video_file = new File(path); }
    public VideoWriter() {}
    public void open(String path) {
        video_file = null;
        video_file = new File(path);
    }

    public void check_and_create() throws IOException {
        if (!video_file.exists()) {
            video_file.createNewFile();
        }
    }

    public void start_encoder(int fps) throws IOException {
        if (encoder == null) {
            encoder = AWTSequenceEncoder.createSequenceEncoder(video_file, fps);
        }
    }

    public void encode_image(BufferedImage image) throws IOException {
        encoder.encodeImage(image);
    }

    public void stop_encoder() throws IOException {
        if (encoder != null) {
            encoder.finish();
            encoder = null;
        }
    }
}