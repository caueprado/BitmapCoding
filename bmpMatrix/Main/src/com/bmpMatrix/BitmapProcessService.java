package com.bmpMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BitmapProcessService {

    public void process(BufferedImage srcImage, int targetSize) {
        try {
            System.out.println("process");
            final int[] newImage = resize(srcImage, targetSize);
            write(getHexBitmap(newImage),srcImage.getHeight());
        } catch (Exception e) {
            System.out.println("error 1" + e.getMessage());
        }
    }

    private void write(List<String> image, int radix) {
        System.out.println("writing");
        int size = image.size();

        final int dimension = image.size() / radix;

        try (final BufferedWriter bw = Files.newBufferedWriter(Paths.get(String.format("qr_%s.csv", LocalDateTime.now())))) {
            for (int i = 0; i < image.size(); i++) {
                bw.write(image.get(i)+",");
                if(i%dimension==0) {
                    bw.newLine();
                }
            }
            bw.flush();
        } catch (Exception e) {
            System.out.println("error 2" + e.getMessage());
        }
    }

    public void coloredBmp(BufferedImage image) {
        final int height = image.getHeight();
        final int width = image.getWidth();

        int rgb = 0, red, green, blue;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rgb = image.getRGB(i, j);
                red = (rgb >> 16) & 0x000000FF;
                green = (rgb >> 8) & 0x000000FF;
                blue = (rgb) & 0x000000FF;
            }
        }
    }

    public List<String> getHexBitmap(int[] newImage) {
        System.out.println("transform");
        final int dimension = newImage.length;

        String[] temp = new String[dimension];
        for (int i = 0; i < dimension; i++) {
            temp[i] = (Integer.toUnsignedLong(0) & 0xff) == 0 ? "0x00" : "0xff";
        }
        return Arrays.asList(temp);
    }

    private int[] resize(BufferedImage srcImage, int targetSize) throws IOException {
        System.out.println("resize");
        final int height = srcImage.getHeight();
        final int width = srcImage.getWidth();

        final byte[] bmp = toByteArray(srcImage, "bmp");
        final int[] newBmp = new int[targetSize * targetSize];

        final double x_ratio = width / targetSize;
        final double y_ratio = height / targetSize;

        for (int h = 0; h < targetSize; h++) {
            for (int w = 0; w < targetSize; w++) {
                double py = Math.floor(y_ratio * w);
                double px = Math.floor(x_ratio * h);

                newBmp[(h * targetSize) + w] = bmp[(int) ((py * width) + px)];
            }
        }
        return newBmp;
    }

    private static byte[] toByteArray(BufferedImage image, String type) throws IOException {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }
}
