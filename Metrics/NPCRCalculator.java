import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class NPCRCalculator {

    public static double calculateNPCR(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();
        int diffPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel1 = img1.getRGB(x, y) & 0xFF; // Assuming grayscale image
                int pixel2 = img2.getRGB(x, y) & 0xFF;

                if (pixel1 != pixel2) {
                    diffPixels++;
                }
            }
        }

        int totalPixels = width * height;
        return (diffPixels * 100.0) / totalPixels;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter path of the first image (original): ");
            String path1 = scanner.nextLine();

            System.out.print("Enter path of the second image (encrypted): ");
            String path2 = scanner.nextLine();

            File file1 = new File(path1);
            File file2 = new File(path2);

            BufferedImage img1 = ImageIO.read(file1);
            BufferedImage img2 = ImageIO.read(file2);

            if (img1 == null || img2 == null) {
                System.out.println("One or both images could not be read. Make sure the paths and formats are correct.");
                return;
            }

            if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
                System.out.println("Error: Images must be of the same size.");
                return;
            }

            double npcr = calculateNPCR(img1, img2);
            System.out.printf("NPCR = %.4f%%\n", npcr);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
