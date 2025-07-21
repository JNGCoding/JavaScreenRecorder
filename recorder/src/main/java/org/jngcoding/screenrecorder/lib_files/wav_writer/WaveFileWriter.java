package org.jngcoding.screenrecorder.lib_files.wav_writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class WaveFileWriter {
    private File openedFile = null;
    private FileOutputStream fos = null;
    private int bytesWritten = 0;
    private WaveFileConfig wav_config = null;

    public WaveFileWriter(WaveFileConfig config) {
        wav_config = config;
    }

    public WaveFileWriter() {}

    public void setConfig(WaveFileConfig config) {
        wav_config = config;
    }

    private byte[] reverseArray(byte[] array) {
        byte[] reversedArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            reversedArray[i] = array[array.length - 1 - i];
        }
        return reversedArray;
    }

    private byte[] intToBytes(int number) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(number);
        return buffer.array();
    }

    private byte[] GetHeader(WaveFileConfig config) {
        byte[] header = new byte[44];

        System.arraycopy(config.ChunkID.getBytes(), 0, header, 0, 4);//4
        System.arraycopy(reverseArray(intToBytes(config.ChunkSize)), 0, header, 4, 4);//4
        System.arraycopy(config.format.getBytes(), 0, header, 8, 4);//4
        System.arraycopy(config.SubChunkID.getBytes(), 0, header, 12, 4);//4
        System.arraycopy(reverseArray(intToBytes(config.SubChunkSize)), 0, header, 16, 4);//4
        System.arraycopy(reverseArray(intToBytes(config.AudioFormat)), 0, header, 20, 2);//2
        System.arraycopy(reverseArray(intToBytes(config.Num_Channels)), 0, header, 22, 2);//2
        System.arraycopy(reverseArray(intToBytes(config.SampleRate)), 0, header, 24, 4);//4
        System.arraycopy(reverseArray(intToBytes(config.ByteRate)), 0, header, 28, 4);//4
        System.arraycopy(reverseArray(intToBytes(config.BlockAlign)), 0, header, 32, 2);//2
        System.arraycopy(reverseArray(intToBytes(config.BitsPerSample)), 0, header, 34, 2);//2
        System.arraycopy(config.SubChunk2ID.getBytes(), 0, header, 36, 4);//4
        System.arraycopy(reverseArray(intToBytes(config.SubChunk2Size)), 0, header, 40, 4);//4

        return header;
    }

    public void openFile(String path) throws IOException {
        openedFile = new File(path);
        if (!openedFile.exists()) {
            openedFile.createNewFile();
        }

        fos = new FileOutputStream(openedFile);
        fos.write(GetHeader(wav_config));
    }

    public void write8(byte data) throws IOException {
        if (fos != null) {
            fos.write((int) data);
            bytesWritten++;
        }
    }

    public void write16(int data) throws IOException {
        if (fos != null) {
            write8((byte)(data & 0xFF));
            write8((byte)((data >> 8) & 0xFF));
        }
    }

    public void write32(int data) throws IOException {
        if (fos != null) {
            write8((byte)(data & 0xFF));
            write8((byte)((data >> 8) & 0xFF));
            write8((byte)((data >> 16) & 0xFF));
            write8((byte)((data >> 24) & 0xFF));
        }
    }

    public void write(byte[] b) throws IOException {
        if (fos != null) {
            fos.write(b);
            bytesWritten += b.length;
        }
    }

    public void write(byte[] b, int start, int end) throws IOException {
        if (fos != null) {
            fos.write(b, start, end);
            bytesWritten += end - start;
        }
    }

    public void finishFile() throws IOException {
        if (fos != null) 
        {
            wav_config.SubChunk2Size = bytesWritten * wav_config.Num_Channels * wav_config.BitsPerSample / 8;
            wav_config.ChunkSize = 4 + 8 + wav_config.SubChunkSize + 8 + wav_config.SubChunk2Size;

            fos.close();
            fos = null;

            try (RandomAccessFile ras = new RandomAccessFile(openedFile, "rw")) {
                ras.seek(4);
                ras.write(reverseArray(intToBytes(wav_config.ChunkSize)));
                ras.seek(40);
                ras.write(reverseArray(intToBytes(wav_config.SubChunk2Size)));
            }

            openedFile = null;
            bytesWritten = 0;
        }
    }
}
