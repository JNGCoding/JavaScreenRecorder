package org.jngcoding.screenrecorder;

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
    public int RefreshRate;
    public boolean SystemAudio;

    public VideoConfig(String path) throws JsonException {
        try (FileReader reader = new FileReader(new File(path))) {
            parent = (JsonObject) Jsoner.deserialize(reader);
        } catch (IOException ignored) {}

        if (parent != null) {
            FPS = ((BigDecimal) parent.get("FPS")).intValue();  // I don't know but the FPS attribute is automatically parsed to be a BigDecimal so I have to use this instead.
            SystemAudio = (boolean) parent.get("SystemAudio");
            RefreshRate = ((BigDecimal) parent.get("VideoStream_FPS")).intValue();
            FrameTime = 1000 / FPS;
        }
    }

    public int getFPS() {
        return FPS;
    }

    public boolean getSystemAudio() {
        return SystemAudio;
    }

    public int getFrameTime() {
        return FrameTime;
    }

    public int getSystemRefreshRate() {
        return RefreshRate;
    }

    @Override
    public String toString() {
        return parent.toString();
    }
}
