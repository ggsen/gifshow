package com.goodluck;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AsciiShow {
    private static final String ASCII_CODE = "@&#$%*+o,`.";

    public static void main(String[] args) {
        URL gifUrl = ClassLoader.getSystemResource("resources/img.gif");
        parseToAscii(gifUrl.getPath());
    }

    private static void parseToAscii(String gifPath) {
        final StringBuilder textBuilder = new StringBuilder();
        MainFrame asciiShowframe = new MainFrame();
        Thread gifThread = new Thread(() -> {
            try (InputStream inputStream = new FileInputStream(new File(gifPath))) {
                GIFImageReader gifReader = createGifReader(inputStream);
                int imageNum = gifReader.getNumImages(true);
                for (; ; ) {
                    for (int i = 0; i < imageNum; i++) {
                        BufferedImage image = gifReader.read(i, null);
                        transferAsciiCode(image, textBuilder);

                        asciiShowframe.resetText(textBuilder.toString());
                        textBuilder.delete(0, textBuilder.length());
                        Thread.sleep(60L);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        gifThread.start();
    }

    /**
     * 取得gif图像输入流
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static GIFImageReader createGifReader(InputStream inputStream) throws IOException {
        ImageInputStream in = ImageIO.createImageInputStream(inputStream);
        ImageReaderSpi gifSpi = new GIFImageReaderSpi();
        GIFImageReader gifReader = (GIFImageReader) gifSpi.createReaderInstance();
        gifReader.setInput(in);
        return gifReader;
    }

    /**
     * 转换为ascii字符
     * @param image 图像对象
     * @param textBuilder 字符串拼接对象
     */
    private static void transferAsciiCode(BufferedImage image, StringBuilder textBuilder) {
        for (int y = 0; y < image.getHeight(); y += 5.5) {
            for (int x = 0; x < image.getWidth(); x += 3) {
                int pixel = image.getRGB(x, y);
                int r = (pixel & 0xFF0000) >> 16, g = (pixel & 0xFF00) >> 8, b = pixel & 0xFF;
                float gray = 0.31F * r + 0.57F * g + 0.12F * b;
                int index = Math.round(gray * (ASCII_CODE.length() + 1) / 255);
                String code = index >= ASCII_CODE.length() ? " " : String.valueOf(ASCII_CODE.charAt(index));
                // System.out.print(code);
                textBuilder.append(code);
            }
            // System.out.println();
            textBuilder.append("\r\n");
        }
    }
}