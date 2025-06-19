import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class PSNR {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Input image paths
            System.out.print("Enter the full path of the original color image: ");
            String originalImagePath = sc.nextLine();
            System.out.print("Enter the full path of the encrypted color image: ");
            String encryptedImagePath = sc.nextLine();
            System.out.print("Enter the full path of the decrypted color image: ");
            String decryptedImagePath = sc.nextLine();

            // Load images
            BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
            BufferedImage encryptedImage = ImageIO.read(new File(encryptedImagePath));
            BufferedImage decryptedImage = ImageIO.read(new File(decryptedImagePath));

            // Calculate PSNR between Original and Encrypted
            double psnr_OE = calculatePSNR(originalImage, encryptedImage);
            System.out.println("PSNR (Original vs Encrypted): " + psnr_OE + " dB");

            // Calculate PSNR between Original and Decrypted
            double psnr_OD = calculatePSNR(originalImage, decryptedImage);
            System.out.println("PSNR (Original vs Decrypted): " + psnr_OD + " dB");

        } catch (IOException e) {
            System.out.println("Error loading images: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    // Method to calculate PSNR for color images
    public static double calculatePSNR(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();

        double mse = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = rgb1 & 0xFF;

                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = rgb2 & 0xFF;

                mse += Math.pow(r1 - r2, 2);
                mse += Math.pow(g1 - g2, 2);
                mse += Math.pow(b1 - b2, 2);
            }
        }

        // Normalize MSE by number of values (3 channels per pixel)
        mse /= (width * height * 3);

        if (mse == 0) {
            return Double.POSITIVE_INFINITY;  // Images are identical
        }

        // Compute PSNR
        return 10 * Math.log10(Math.pow(255, 2) / mse);
    }
}
