package cab302.viewer.gui;

import cab302.viewer.exceptions.BadImageFormatException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class ImageGenerator {

    public boolean isBase64EncodedImage(String imageData) {

        return !imageData.startsWith("https");
    }
    public ImageGenerator() {
    }

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

    public BufferedImage downloadImage(String imageInfo) throws BadImageFormatException, IOException {
        if (!isBase64EncodedImage(imageInfo)) {
            BufferedImage img;
            img = ImageIO.read(new URL("https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download"));

            return img;
        }
        else {
            throw new BadImageFormatException("Image was found to be a format other than a URL, consider changing to decodeDataString().");
        }


    }
}
