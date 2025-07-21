package org.jngcoding.screenrecorder.lib_files;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Capture {
    public static BufferedImage capture(GraphicsDevice gd) throws AWTException {
        DisplayMode mode = gd.getDisplayMode();
        Robot robot = new Robot();
        return robot.createScreenCapture(new Rectangle(0, 0, mode.getWidth(), mode.getHeight()));
    }

    public static BufferedImage capture() throws AWTException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Robot robot = new Robot();
        return robot.createScreenCapture(new Rectangle(screenSize));
    }
}