import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class UACICalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Get image paths from user
            System.out.print("Enter original image path: ");
            String originalPath = scanner.nextLine();
            
            System.out.print("Enter encrypted image path: ");
            String encryptedPath = scanner.nextLine();

            // Load images
            BufferedImage original = ImageIO.read(new File(originalPath));
            BufferedImage encrypted = ImageIO.read(new File(encryptedPath));
            
            // Calculate UACI
            double uaci = calculateUACI(original, encrypted);
            
            // Display results
            System.out.println("\n=== UACI Analysis ===");
            System.out.printf("UACI Value: %.4f%%\n", uaci);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static double calculateUACI(BufferedImage original, BufferedImage encrypted) {
        // Validate dimensions
        if (original.getWidth() != encrypted.getWidth() || 
            original.getHeight() != encrypted.getHeight()) {
            throw new IllegalArgumentException("Image dimensions must match");
        }

        double totalDiff = 0;
        int width = original.getWidth();
        int height = original.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int origRGB = original.getRGB(x, y);
                int encRGB = encrypted.getRGB(x, y);

                // Calculate absolute differences for each channel
                totalDiff += Math.abs(((origRGB >> 16) & 0xFF) - ((encRGB >> 16) & 0xFF)) / 255.0;
                totalDiff += Math.abs(((origRGB >> 8) & 0xFF) - ((encRGB >> 8) & 0xFF)) / 255.0;
                totalDiff += Math.abs((origRGB & 0xFF) - (encRGB & 0xFF)) / 255.0;
            }
        }

        return (totalDiff / (width * height * 3)) * 100;
    }

   
}