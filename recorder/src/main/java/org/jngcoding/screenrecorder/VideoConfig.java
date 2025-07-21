package org.jngcoding.screenrecorder;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class VideoConfig {
    public JsonObject parent = null;
    public int FPS;
    public int FrameTime;
    public boolean SystemAudio;
    public Rectangle CaptureArea;

    public VideoConfig(String path) throws JsonException {
        try (FileReader reader = new FileReader(new File(path))) {
            parent = (JsonObject) Jsoner.deserialize(reader);
        } catch (IOException ignored) {}

        if (parent != null) {
            FPS = ((BigDecimal) parent.get("FPS")).intValue();  // I don't know but the FPS attribute is automatically parsed to be a BigDecimal so I have to use this instead.
            SystemAudio = (boolean) parent.get("SystemAudio");
            FrameTime = 1000 / FPS;

            Object rect = parent.get("CaptureArea");
            if (rect instanceof JsonObject rectangle) {
                if ((boolean) rectangle.get("FullScreen")) {
                    CaptureArea = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                } else {
                    CaptureArea = new Rectangle();
                    CaptureArea.x = ((BigDecimal) rectangle.get("X")).intValue();
                    CaptureArea.y = ((BigDecimal) rectangle.get("Y")).intValue();
                    CaptureArea.width = ((BigDecimal) rectangle.get("W")).intValue();
                    CaptureArea.height = ((BigDecimal) rectangle.get("H")).intValue();
                }
            }
        }
    }

    public int getFPS() {
        return FPS;
    }

    public Rectangle getCaptureArea() {
        return CaptureArea;
    }

    public boolean getSystemAudio() {
        return SystemAudio;
    }

    public int getFrameTime() {
        return FrameTime;
    }

    @Override
    public String toString() {
        return parent.toString();
    }
}
