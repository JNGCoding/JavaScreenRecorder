package org.jngcoding.screenrecorder.lib_files.wav_writer;

public class WaveFileConfig {
    // RIFF
    public String ChunkID;
    public int ChunkSize;
    public String format;

    // FMT
    public String SubChunkID;
    public int SubChunkSize;
    public int AudioFormat;
    public int Num_Channels;
    public int SampleRate;
    public int ByteRate;
    public int BlockAlign;
    public int BitsPerSample;

    // SubChunk2
    public String SubChunk2ID;
    public int SubChunk2Size;

    public WaveFileConfig(int SubChunkSize_, int AudioFormat_, int Num_Channels_, int SampleRate_, int BitsPerSample_) {
        ChunkID = "RIFF";
        ChunkSize = Integer.MAX_VALUE;
        format = "WAVE";

        SubChunkID = "fmt ";
        SubChunkSize = SubChunkSize_;
        AudioFormat = AudioFormat_;
        Num_Channels = Num_Channels_;
        SampleRate = SampleRate_;
        BitsPerSample = BitsPerSample_;
        ByteRate = SampleRate * Num_Channels * BitsPerSample / 8;
        BlockAlign = Num_Channels * BitsPerSample / 8;

        SubChunk2ID = "data";
        SubChunk2Size = Integer.MAX_VALUE;
    }

    public static WaveFileConfig getDefaultConfiguration() {
        return new WaveFileConfig(16, 1, 2, 4000, 16);
    }
}