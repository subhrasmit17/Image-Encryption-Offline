import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Image_Encryption {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Ask the user for the image file path
            System.out.print("Enter the full path of the image file: ");
            String image_path = sc.nextLine(); // Get image path input from the user

            // Load the image from the specified file path
            File input_file = new File(image_path);
            if (!input_file.exists()) {
                System.out.println("Error: File does not exist at the specified path.");
                return;
            }

            BufferedImage image = ImageIO.read(input_file);
            if (image == null) {
                System.out.println("Error: Unsupported or corrupted image file.");
                return;
            }

            // Perform histogram equalization
            BufferedImage eq_image = histogram_equalization(image);

            // Get encryption key from the user
            System.out.print("Enter encryption key (integer): ");
            int key = sc.nextInt();
            int k = key; // Store the original key

            // Perform encryption
            BufferedImage encrypt_image = encrypt_image(eq_image, key, k);

            // Ask the user for the output file path
            System.out.print("Enter the output path for the encrypted image (including the file name): ");
            sc.nextLine(); // Consume the newline left from the previous input
            String output_path = sc.nextLine();

            // Save the encrypted image
            File output_file = new File(output_path);
            ImageIO.write(encrypt_image, "jpg", output_file);

            System.out.println("Image encryption completed successfully!");
            System.out.println("Encrypted image saved at: " + output_path);

        } catch (IOException e) {
            System.out.println("Error loading or saving the image: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    private static BufferedImage histogram_equalization(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] histogram = new int[256];
        int total_pixels = width * height;

        // Calculate histogram
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y) & 0xFF;
                histogram[pixel]++;
            }
        }

        // Calculate cumulative distribution function (CDF)
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }

        // Normalize CDF
        int[] eq = new int[256];
        for (int i = 0; i < 256; i++) {
            eq[i] = Math.round((float) cdf[i] / total_pixels * 255);
        }

        // Apply histogram equalization
        BufferedImage equalized_image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y) & 0xFF;
                int equalized_pixel = eq[pixel];
                equalized_image.setRGB(x, y, equalized_pixel);
            }
        }
        return equalized_image;
    }

    private static BufferedImage encrypt_image(BufferedImage image, int key, int k) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage encrypted_image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y) & 0xFF;
                int encrypted_pixel = ((pixel * key)^(key*key+149)) % 256; //randomly choosen 3 digit prime number -149
                int n = pixel % 10;
                if (key == 0) 
                key = k;
                key = (key) * (key + n + (x * y));
                encrypted_image.setRGB(x, y, encrypted_pixel);
            }
        }
        return encrypted_image;
    }
}
