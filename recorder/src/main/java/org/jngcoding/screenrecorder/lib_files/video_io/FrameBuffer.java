package org.jngcoding.screenrecorder.lib_files.video_io;

import java.awt.image.BufferedImage;
import java.util.concurrent.LinkedBlockingQueue;

public class FrameBuffer extends LinkedBlockingQueue<BufferedImage> {
    public FrameBuffer(int allocation_size) {
        super(allocation_size);
    }
}
