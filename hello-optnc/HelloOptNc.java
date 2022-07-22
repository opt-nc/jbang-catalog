///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.concurrent.Callable;

@Command(name = "hello-optnc", mixinStandardHelpOptions = true, version = "hello-optnc 0.1",
        description = "HelloOptNc made with jbang")
class HelloOptNc implements Callable<Integer> {

    @Parameters(index = "0", description = "The greeting to print", defaultValue = "J'Bang!")
    private String greeting;

    public static void main(String... args) {
        int exitCode = new CommandLine(new HelloOptNc()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        printAsciiArt("OPT-NC <3 " + greeting);
        return 0;
    }

    public static void printAsciiArt(String text) throws IOException {

        int width = 200;
        int height = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("Sans Serif", Font.BOLD, 15));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(text, 5, 15);

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {

                sb.append(image.getRGB(x, y) == -16777216 ? " " : "o");

            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(sb);
        }

    }
}
