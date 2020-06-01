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
     *
     * @param imageData The image that is provided
     * @return A boolean that symbolises whether the image is base64 or not
     * @throws BadImageFormatException Throws exception if the image format is not Base64 or a URL
     */

    public boolean isBase64EncodedImage(String imageData) {

        return !imageData.startsWith("https");
    }
    /**
     * Image Generator Constructor
     */
    public ImageGenerator() {
    }
    /**
     * Decode the Base64 encoded data string provided
     * @param imageInfo Base64 encoding image data string
     * @return BufferedImage containing the decoded image
     * @throws IOException if an error occurs while reading the file
     * @throws BadImageFormatException if the image is not Base64
     */
    public BufferedImage decodeDataString(String imageInfo) throws IOException, BadImageFormatException {

        if (isBase64EncodedImage(imageInfo)) {
            // Create local variables to store data and images
            BufferedImage img;
            byte[] imageDataBytes;

            imageDataBytes = Base64.getDecoder().decode(imageInfo);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageDataBytes);
            img = ImageIO.read(byteArrayInputStream);

            return img;
        } else {
            throw new BadImageFormatException("Image was found to be a URL, consider changing to downloadImage().");
        }

    }
    /**
     * Download image from URL string provided
     * @param imageInfo URL to image
     * @return BufferedImage containing the downloaded image
     * @throws IOException if an error occurs while reading the file
     * @throws BadImageFormatException if the image is not Base64
     */
    public BufferedImage downloadImage(String imageInfo) throws BadImageFormatException, IOException {
        if (!isBase64EncodedImage(imageInfo)) {
            BufferedImage img;
            img = ImageIO.read(new URL(imageInfo));

            return img;
        } else {
            throw new BadImageFormatException("Image was found to be a format other than a URL, consider changing to decodeDataString().");
        }


    }
}