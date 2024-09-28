import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class NPCR {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Ask the user for the first image file path
            System.out.print("Enter the full path of the first encrypted image file: ");
            String image_path1 = sc.nextLine();

            // Ask the user for the second image file path
            System.out.print("Enter the full path of the second encrpted image file: ");
            String image_path2 = sc.nextLine();

            // Load both images
            BufferedImage image1 = ImageIO.read(new File(image_path1));
            BufferedImage image2 = ImageIO.read(new File(image_path2));

            if (image1 == null || image2 == null) {
                System.out.println("Error: Unsupported or corrupted image file.");
                return;
            }

            // Check if both images have the same dimensions
            if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
                System.out.println("Error: Images must have the same dimensions.");
                return;
            }

            // Compute NPCR (Number of Pixel Change Rate)
            double npcr = calc_npcr(image1, image2);
            System.out.printf("NPCR value: %.2f%%\n", npcr);

        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    // Method to compute NPCR between two images
    public static double calc_npcr(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();
        int total= width * height; //total pixels
        int c = 0; //changed pixels

        // Iterate through each pixel of the images
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p1 = image1.getRGB(x, y) & 0xFF; // Extract intensity value for image1
                int p2 = image2.getRGB(x, y) & 0xFF; // Extract intensity value for image2

                // Check if pixel values differ
                if (p1 != p2) {
                    c++;
                }
            }
        }

        // Calculate NPCR as a percentage
        double npcr = (double) c / total * 100;
        return npcr;
    }
}
