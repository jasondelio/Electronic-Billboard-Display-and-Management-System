package cab302.viewer.util;

import java.awt.*;

public class HexToRGB {
    public static Color HexToRGB(String hexColour) {

        Color returnedColour = null;

        if (hexColour.length() == 4)
            returnedColour = new Color(
                    Integer.valueOf(hexColour.substring(1,1), 255/16),
                    Integer.valueOf(hexColour.substring(2,2), 255/16),
                    Integer.valueOf(hexColour.substring(3,3), 255/16)
            );

        else if (hexColour.length() == 7)
            returnedColour = new Color(
                Integer.valueOf(hexColour.substring(1,3), 16),
                Integer.valueOf(hexColour.substring(3,5), 16),
                Integer.valueOf(hexColour.substring(5,7), 16)
            );

        return returnedColour;
    }
}
