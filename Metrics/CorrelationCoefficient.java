import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class CorrelationCoefficient {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the full path of the original color image: ");
            String originalImagePath = sc.nextLine();
            System.out.print("Enter the full path of the encrypted color image: ");
            String encryptedImagePath = sc.nextLine();
            System.out.print("Enter the full path of the decrypted color image: ");
            String decryptedImagePath = sc.nextLine();

            BufferedImage originalImage = loadImage(originalImagePath, "original");
            BufferedImage encryptedImage = loadImage(encryptedImagePath, "encrypted");
            BufferedImage decryptedImage = loadImage(decryptedImagePath, "decrypted");

            calculateAndDisplayCorrelation(originalImage, "Original");
            calculateAndDisplayCorrelation(encryptedImage, "Encrypted");
            calculateAndDisplayCorrelation(decryptedImage, "Decrypted");

        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    public static BufferedImage loadImage(String path, String type) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        if (image == null) {
            throw new IOException("Error: The " + type + " image could not be loaded.");
        }
        return image;
    }

    public static void calculateAndDisplayCorrelation(BufferedImage image, String imageType) {
        System.out.println("\nCorrelation coefficients for " + imageType + " image:");

        for (String channel : new String[]{"Red", "Green", "Blue"}) {
            System.out.println("\n[" + channel + " Channel]");
            double horizontal = calculateCorrelation(image, "horizontal", channel);
            double vertical = calculateCorrelation(image, "vertical", channel);
            double diagonal = calculateCorrelation(image, "diagonal", channel);

            System.out.println("Horizontal Correlation: " + horizontal);
            System.out.println("Vertical Correlation: " + vertical);
            System.out.println("Diagonal Correlation: " + diagonal);
        }
    }

    public static double calculateCorrelation(BufferedImage image, String direction, String channel) {
        int width = image.getWidth();
        int height = image.getHeight();
        int T = 0;
        double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image.getRGB(x, y);
                int rgb2 = 0;

                if (direction.equals("horizontal") && x < width - 1)
                    rgb2 = image.getRGB(x + 1, y);
                else if (direction.equals("vertical") && y < height - 1)
                    rgb2 = image.getRGB(x, y + 1);
                else if (direction.equals("diagonal") && x < width - 1 && y < height - 1)
                    rgb2 = image.getRGB(x + 1, y + 1);
                else
                    continue;

                int value1 = getColorComponent(rgb1, channel);
                int value2 = getColorComponent(rgb2, channel);

                sumX += value1;
                sumY += value2;
                sumX2 += value1 * value1;
                sumY2 += value2 * value2;
                sumXY += value1 * value2;
                T++;
            }
        }

        if (T == 0) return 0;

        double meanX = sumX / T;
        double meanY = sumY / T;
        double varianceX = (sumX2 / T) - (meanX * meanX);
        double varianceY = (sumY2 / T) - (meanY * meanY);
        double covariance = (sumXY / T) - (meanX * meanY);

        return covariance / Math.sqrt(varianceX * varianceY);
    }

    public static int getColorComponent(int rgb, String channel) {
        switch (channel) {
            case "Red":
                return (rgb >> 16) & 0xFF;
            case "Green":
                return (rgb >> 8) & 0xFF;
            case "Blue":
                return rgb & 0xFF;
            default:
                return 0;
        }
    }
}
