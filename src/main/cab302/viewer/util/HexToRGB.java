package cab302.viewer.util;

import java.awt.*;

public class HexToRGB {

    /**
     * Convert to Hexadecimal colour codes to RGB
     * @param hexColour A hexidecimal colour of length 4 or 7
     * @return RGB Color object
     */
    public static Color HexToRGB(String hexColour) {

        Color returnedColour = null;

        // If the hex colour string has length 4, convert to Color object
        if (hexColour.length() == 4)
            returnedColour = new Color(
                    Integer.valueOf(hexColour.substring(1,1), 255/16),
                    Integer.valueOf(hexColour.substring(2,2), 255/16),
                    Integer.valueOf(hexColour.substring(3,3), 255/16)
            );

        // Else, if the hex colour string has length 7, convert to Color
        // object
        else if (hexColour.length() == 7)
            returnedColour = new Color(
                Integer.valueOf(hexColour.substring(1,3), 16),
                Integer.valueOf(hexColour.substring(3,5), 16),
                Integer.valueOf(hexColour.substring(5,7), 16)
            );

        return returnedColour;
    }
}
