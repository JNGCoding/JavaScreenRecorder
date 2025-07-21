package org.jngcoding.screenrecorder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import javax.sound.sampled.AudioFormat;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class AudioConfig {
    public JsonObject parent = null;
    public JsonObject TargetAudioConfig;
    public JsonObject SourceAudioConfig;

    public AudioConfig(String path) throws JsonException {
        try (FileReader reader = new FileReader(new File(path))) {
            parent = (JsonObject) Jsoner.deserialize(reader);
        } catch (IOException ignored) {}

        if (parent != null) {
            TargetAudioConfig = (JsonObject) ((JsonObject) parent.get("CurrentAudioLines")).get("AbsorbLine");
            SourceAudioConfig = (JsonObject) ((JsonObject) parent.get("CurrentAudioLines")).get("ReflectLine");
        }
    }

    public String[] getAvailableSourceLines() {
        return (String[]) ((JsonArray) parent.get("AvailableAudioSourceLines")).toArray();
    }

    public String[] getAvailableTargetLines() {
        return (String[]) ((JsonArray) parent.get("AvailableAudioTargetLines")).toArray();
    }

    public AudioFormat getAudioFormatOfAbsorbLine() {
        int sample_rate, bits_per_sample, channels;
        boolean signed, bigEndian;

        JsonObject audioconfig = (JsonObject) TargetAudioConfig.get("AudioFormat");
        sample_rate = ((BigDecimal) audioconfig.get("SampleRate")).intValue();
        bits_per_sample = ((BigDecimal) audioconfig.get("Bits")).intValue();
        channels = ((BigDecimal) audioconfig.get("Channels")).intValue();
        signed = (boolean) audioconfig.get("Signed");
        bigEndian = (boolean) audioconfig.get("BigEndian");

        return new AudioFormat(sample_rate, bits_per_sample, channels, signed, bigEndian);
    }

    public AudioFormat getAudioFormatOfReflectLine() {
        int sample_rate, bits_per_sample, channels;
        boolean signed, bigEndian;

        JsonObject audioconfig = (JsonObject) SourceAudioConfig.get("AudioFormat");
        sample_rate = ((BigDecimal) audioconfig.get("SampleRate")).intValue();
        bits_per_sample = ((BigDecimal) audioconfig.get("Bits")).intValue();
        channels = ((BigDecimal) audioconfig.get("Channels")).intValue();
        signed = (boolean) audioconfig.get("Signed");
        bigEndian = (boolean) audioconfig.get("BigEndian");

        return new AudioFormat(sample_rate, bits_per_sample, channels, signed, bigEndian);
    }

    @Override
    public String toString() {
        return parent.toString();
    }
}
