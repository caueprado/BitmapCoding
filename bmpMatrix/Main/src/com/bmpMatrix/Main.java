package com.bmpMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final BitmapProcessService bitmapProcessService = new BitmapProcessService();

    public static void main(String[] args) {
        try {
            final BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get("qrc.bmp")));

            final ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(image, "bmp", bos);

            bos.flush();

            bitmapProcessService.process(image,48);

            bos.close();
        } catch (IOException e) {
            System.err.println("IO exception");
        }
    }
}
