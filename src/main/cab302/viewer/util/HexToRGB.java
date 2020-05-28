package cab302.viewer.util;

import java.awt.*;

public class HexToRGB {
    public static Color HexToRGB(String hexColour) {
        return new Color(
                Integer.valueOf(hexColour.substring(1,3), 16),
                Integer.valueOf(hexColour.substring(3,5), 16),
                Integer.valueOf(hexColour.substring(5,7), 16)
        );
    }
}
