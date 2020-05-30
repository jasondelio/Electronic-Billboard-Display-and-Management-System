package cab302.viewer.util;

import cab302.viewer.exceptions.MalformedHexadecimalColourException;

import java.awt.*;

public class HexToRGB {

    /**
     * Convert to Hexadecimal colour codes to RGB
     *
     * @param hexColour A hexidecimal colour of length 4 or 7
     * @return RGB Color object
     */
    public static Color HexToRGB(String hexColour) throws MalformedHexadecimalColourException {

        Color returnedColour;
        // If the hex colour string has length 4, convert to Color object
        if (hexColour.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
            returnedColour = Color.decode(hexColour);
        // Else, if the hex colour string has length 7, convert to Color
        // object
        else
            throw new MalformedHexadecimalColourException("The hexadecimal string is not a valid Hexadecimal Colour Code");

        return returnedColour;
    }
}
