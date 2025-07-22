# ScreenRecorderJava
ScreenRecorderJava is a desktop application for Windows that allows you to record your screen and system audio. It features a simple GUI for starting and stopping recordings, and saves output as `.mp4` (video) and `.wav` (audio) files.

## Features

- Record screen video at configurable FPS
- Capture system audio and microphone input
- Simple GUI with start/stop controls
- Output video in MP4 format and audio in WAV format
- Configurable via JSON files

## Project Structure

```
AudioConfig.json
VideoConfig.json
recorder/
    pom.xml
    src/
        main/
            java/
                org/
                    jngcoding/
                        screenrecorder/
        ...
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Build

```sh
cd recorder
mvn package
```

### Run

```sh
java -jar out/ScreenRecorderJava.jar
```

### Configuration

- **AudioConfig.json**: Configure available audio lines and formats.
- **VideoConfig.json**: Set video FPS, audio options and Capture Area.

## Usage

1. Launch the application.
2. Use the GUI to start and stop recording.
3. Choose the output file location when prompted.

## Dependencies

- [json-simple](https://github.com/cliftonlabs/json-simple)
- [jcodec](https://github.com/jcodec/jcodec)

## Notes
- For best results, ensure your audio devices are properly configured.
