package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class ImageGenerator {

    /**
     * Image Generator Constructor
     */
    public ImageGenerator() {}

    /**
     *
     * @param imageData The image that is provided
     * @return A boolean that symbolises whether the image is base64 or not
     * @throws BadImageFormatException Throws exception if the image format is not Base64 or a URL
     */
    public boolean isBase64EncodedImage(String imageData) throws BadImageFormatException {

        // A URL starts with either https or http, check if this holds true
        if (imageData.startsWith("https") || imageData.startsWith("http"))
            return false;

        // Match image data against a regex to test for Base64
        else if (imageData.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$"))
            return true;

        // Otherwise, the image format is not valid
        else
            throw new BadImageFormatException("Not a valid image format");
    }

    /**
     * Decode the Base64 encoded data string provided
     * @param imageData Base64 encoding image data string
     * @return BufferedImage containing the decoded image
     * @throws IOException if an error occurs while reading the file
     * @throws BadImageFormatException if the image is not Base64
     */
    public BufferedImage decodeDataString(String imageData) throws IOException, BadImageFormatException {

        // Check the the data is Base64
        if (this.isBase64EncodedImage(imageData)) {
            // Create local variables to store data and images
            BufferedImage img;
            byte[] imageDataBytes;

            // Decode image from Base64 and create InputStream
            imageDataBytes = Base64.getDecoder().decode(imageData);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageDataBytes);

            // Read image and return it
            img = ImageIO.read(byteArrayInputStream);
            return img;
        } else {
            // If not, throw exception
            throw new BadImageFormatException("Image was found to be a URL, consider changing to downloadImage().");
        }

    }

    /**
     * Download image from URL string provided
     * @param imageData URL to image
     * @return BufferedImage containing the downloaded image
     * @throws IOException if an error occurs while reading the file
     * @throws BadImageFormatException if the image is not Base64
     */
    public BufferedImage downloadImage(String imageData) throws BadImageFormatException, IOException {
        // Check if is URL format
        if (!this.isBase64EncodedImage(imageData)) {
            BufferedImage img;

            // Read and return URL image
            img = ImageIO.read(new URL(imageData));
            return img;
        } else {
            // If not, throw exception
            throw new BadImageFormatException("Image was found to be a format other than a URL, consider changing to decodeDataString().");
        }

    }
}
