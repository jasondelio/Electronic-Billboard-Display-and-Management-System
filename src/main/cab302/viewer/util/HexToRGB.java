package cab302.viewer.util;

import cab302.viewer.exceptions.MalformedHexadecimalColourException;

import java.awt.*;

public class HexToRGB {

    /**
     * Convert to Hexadecimal colour codes to RGB
     *
     * @param hexColour A hexidecimal colour of length 4 or 7
     * @return RGB Color object
     * @throws MalformedHexadecimalColourException if the Hexadecimal colour string provided is invalid
     */
    public static Color HexToRGB(String hexColour) throws MalformedHexadecimalColourException {

        Color returnedColour;
        // If the hexadecimal colour matches a regex, parse it as a colour
        if (hexColour.matches("^#([A-Fa-f0-9]{6})$"))
            returnedColour = Color.decode(hexColour);
        else if (hexColour.matches("^#([A-Fa-f0-9]{3})$"))
            returnedColour = Color.decode(
                    "#" +
                            hexColour.substring(1,2) + hexColour.substring(1,2) +
                            hexColour.substring(2,3) + hexColour.substring(2,3) +
                            hexColour.substring(3,4) + hexColour.substring(3,4)
            );
        // Otherwise, throw an exception
        else
            throw new MalformedHexadecimalColourException("The hexadecimal string is not a valid Hexadecimal Colour Code");

        return returnedColour;
    }
}
