import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class labFourier {
    static int height;
    static int width;
    static String fileNameBMP = "Madagascar.bmp";
    static String resultFileNameBMP = "Resized_" + fileNameBMP;

    public static void main() throws IOException {
        File file = new File(fileNameBMP);
        BufferedImage img = ImageIO.read(file);
        width = img.getWidth();
        height = img.getHeight();
        //Resize Parameters     n - Width; M - Height
        double n = 2;
        double m = 0.5;
        int newWidth = (int) (width*n);
        int newHeight = (int) (height*m);
        BufferedImage result = new BufferedImage(newWidth, newHeight, img.getType());
        int[][] pixels = new int[height][width];
        getPixelsRedux(pixels, img);
        int[][] ResPixels = KotelnikovWidth(pixels, n);
        width = (int) (width*n);
        ResPixels = KotelnikovHeight(ResPixels, m);
        height = (int) (height*m);

        for(int i = 0; i < ResPixels.length; i++) {
            for (int j = 0; j < ResPixels[i].length; j++) {
                result.setRGB(j, i, ResPixels[i][j]);
            }
        }
        File output = new File(resultFileNameBMP);
        ImageIO.write(result, "bmp", output);

    }

    public static int[][] KotelnikovHeight(int[][] pixels, double y) {
        int[][] pixelResult = new int[(int) (height*y)][width];
        for(int x = 0; x < width; ++x) {
            for(int w = 0; w < height*y; ++w) {
                double Redsum = 0;
                double Greensum = 0;
                double Bluesum = 0;
                double dt;
                if(w == 0)
                    dt = 0.00001/y;
                else dt = w/y;
                for (int k = 0; k < height; ++k) {
                    Color RGB = new Color(pixels[k][x]);
                    Redsum += RGB.getRed()*sinc(Math.PI*(dt - k));
                    Greensum += RGB.getGreen()*sinc(Math.PI*(dt - k));
                    Bluesum += RGB.getBlue()*sinc(Math.PI*(dt - k));
                }
                Redsum = Recovery(Redsum);
                Greensum = Recovery(Greensum);
                Bluesum = Recovery(Bluesum);
                Color RGB = new Color((int) Redsum, (int) Greensum, (int) Bluesum);
                pixelResult[w][x] = RGB.getRGB();
            }
        }
        return pixelResult;
    }

    public static int[][] KotelnikovWidth(int[][] pixels, double x) {
        int[][] pixelsResult = new int[height][(int) (width*x)];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < (int) width*x; j++) {
                double Redsum = 0;
                double Greensum = 0;
                double Bluesum = 0;
                double dt;
                if(j == 0)
                    dt = 0.00001/x;
                else dt = j/x;
                for (int n = 0; n < width; n++) {
                    Color RGB = new Color(pixels[i][n]);
                    Redsum += RGB.getRed()*sinc(Math.PI*((dt) - n));
                    Greensum += RGB.getGreen()*sinc(Math.PI*((dt) - n));
                    Bluesum += RGB.getBlue()*sinc(Math.PI*((dt) - n));
                }
                Redsum = Recovery(Redsum);
                Greensum = Recovery(Greensum);
                Bluesum = Recovery(Bluesum);
                Color RGB = new Color((int) Redsum, (int) Greensum, (int) Bluesum);
                pixelsResult[i][j] = RGB.getRGB();
            }
        }
        return pixelsResult;
    }

    public static double Recovery(double RGB) {
        while (RGB > 255)
            RGB--;
        while (RGB < 0)
            RGB++;
        return RGB;
    }

    private static void getPixelsRedux(int[][] pixels, BufferedImage image) {
        for(int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixels[i][j] = image.getRGB(j, i);
            }
        }
    }

    public static double sinc(double k) {
        if(Math.abs(k) < 0.001) {
            return 1;
        } else {
            return Math.sin(k)/k;
        }
    }
}


public class Main {

    public static void main(String[] args) throws IOException {
        labFourier.main();
    }
}
