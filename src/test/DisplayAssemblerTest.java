import cab302.viewer.exceptions.MalformedHexadecimalColourException;
import cab302.viewer.gui.DisplayAssembler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DisplayAssemblerTest {

    SimpleAttributeSet set = new SimpleAttributeSet();

    @BeforeAll
    public void createStyleSet() {
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, "Arial");
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_SingleDigit_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#0",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_TwoDigit_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#01",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_FourDigit_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#01FE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_FiveDigit_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#927FE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonHexadecimalNumber_OutsideOfZeroToF_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#927FXE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonHexadecimalNumber_SpecialChars_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#92^FFE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_GreaterThanSixDigit_MessageColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#927FEE6",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_SingleDigit_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#000",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#D",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_TwoDigit_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#001281",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#39",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_FourDigit_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#01FD1E",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#5FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_FiveDigit_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#927FAE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#39FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonThreeOrSixHexadecimalNumber_GreaterThanSixDigit_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#927FEE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395FADF617530197",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonHexadecimalNumber_OutsideOfZeroToF_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#927FDE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#G95FAD",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void throwsMalformedHexadecimalColourExceptionWhenGivenNonHexadecimalNumber_SpecialChars_InformationColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> {
            DisplayAssembler da = new DisplayAssembler(
                    "Message", "#921FFE",
                    "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                    "Information", "#395F^D",
                    new Dimension(1920, 1080)
            );
        });
    }

    @Test
    public void createsEmptyJLabelWhenZeroLengthImageDataProvided() {

        DisplayAssembler displayAssembler = new DisplayAssembler(
                "Message", "#000",
                "", "Information", "#000",
                new Dimension(1920, 1080)
        );

        assertEquals(new JLabel(), displayAssembler.assemblePictureLabel());
    }

    @Test
    public void createsEmptyJTextPaneWhenZeroLengthStringProvided_Message() {

        DisplayAssembler displayAssembler = new DisplayAssembler(
                "", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Information", "#000",
                new Dimension(1920, 1080)
        );

        assertEquals(new JTextPane(), displayAssembler.assembleMessagePane());
    }

    @Test
    public void createsEmptyJTextPaneWhenZeroLengthStringProvided_Information() {

        DisplayAssembler displayAssembler = new DisplayAssembler(
                "A message string", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "", "#000",
                new Dimension(1920, 1080)
        );

        assertEquals(new JTextPane(), displayAssembler.assembleInformationPane());
    }

    @Test
    public void createsEmptyJLabelWhenNullProvided() {

        DisplayAssembler displayAssembler = new DisplayAssembler(
                "Message", "#000",
                null, "Information", "#000",
                new Dimension(1920, 1080)
        );

        assertEquals(new JLabel(), displayAssembler.assemblePictureLabel());
    }

    @Test
    public void createsEmptyJTextPaneWhenNullProvided_Message() {

        DisplayAssembler displayAssembler = new DisplayAssembler(
                null, "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Information", "#000",
                new Dimension(1920, 1080)
        );

        assertEquals(new JTextPane(), displayAssembler.assembleMessagePane());
    }

    @Test
    public void createsEmptyJTextPaneWhenNullProvided_Information() {

        DisplayAssembler displayAssembler = new DisplayAssembler(
                "A message string", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                null, "#000",
                new Dimension(1920, 1080)
        );

        assertEquals(new JTextPane(), displayAssembler.assembleInformationPane());
    }

    @Test
    public void createsCorrectTextPane_TextOnly_Message() {
        JTextPane testPane = new DisplayAssembler(
                "A message string", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Text goes here", "#000",
                new Dimension(1920, 1080)
        ).assembleMessagePane();

        JTextPane expectedPane = new JTextPane();
        expectedPane.setText("A message string");
        StyleConstants.setFontSize(this.set, 84);
        StyleConstants.setForeground(this.set, new Color(0,0,0));
        expectedPane.setParagraphAttributes(this.set, true);

        assertEquals(expectedPane, testPane);
    }

    @Test
    public void createsCorrectTextPane_TextOnly_Information() {
        JTextPane testPane = new DisplayAssembler(
                "A message string", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Text goes here", "#000",
                new Dimension(1920, 1080)
        ).assembleInformationPane();

        JTextPane expectedPane = new JTextPane();
        expectedPane.setText("Text goes here");
        StyleConstants.setFontSize(this.set, 36);
        StyleConstants.setForeground(this.set, new Color(0,0,0));
        expectedPane.setParagraphAttributes(this.set, true);

        assertEquals(expectedPane, testPane);
    }

    @Test
    public void createsCorrectTextPane_TextAndColour_Message() {
        JTextPane testPane = new DisplayAssembler(
                "A message string", "#FDF",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Text goes here", "#000",
                new Dimension(1920, 1080)
        ).assembleMessagePane();

        JTextPane expectedPane = new JTextPane();
        expectedPane.setText("A message string");
        StyleConstants.setFontSize(this.set, 84);
        StyleConstants.setForeground(this.set, new Color(255,221,255));
        expectedPane.setParagraphAttributes(this.set, true);

        assertEquals(expectedPane, testPane);
    }

    @Test
    public void createsCorrectTextPane_TextAndColour_Information() {
        JTextPane testPane = new DisplayAssembler(
                "A message string", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Text goes here", "#FFF",
                new Dimension(1920, 1080)
        ).assembleInformationPane();

        JTextPane expectedPane = new JTextPane();
        expectedPane.setText("Text goes here");
        StyleConstants.setFontSize(this.set, 36);
        StyleConstants.setForeground(this.set, new Color(255,255,255));
        expectedPane.setParagraphAttributes(this.set, true);

        assertEquals(expectedPane, testPane);
    }

    @Test
    public void createsCorrectImageLabel_URL() throws IOException {
        JLabel testLabel = new DisplayAssembler(
                "A message string", "#000",
                "https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download",
                "Text goes here", "#FFF",
                new Dimension(1920, 1080)
        ).assemblePictureLabel();

        BufferedImage picture = ImageIO.read(new URL("https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download"));
        int resizedWidth = picture.getWidth();
        int resizedHeight = picture.getHeight();
        Dimension screenSize = new Dimension(1920, 1080);

        if (picture.getWidth() != screenSize.width / 2) {
            resizedWidth = screenSize.width / 2;
            resizedHeight = (resizedWidth * picture.getHeight()) / picture.getWidth();
        }

        if (picture.getHeight() != screenSize.height / 2) {
            resizedHeight = screenSize.height / 2;
            resizedWidth = (resizedHeight * picture.getWidth()) / picture.getHeight();
        }

        Image resizedImage = picture.getScaledInstance(
                resizedWidth,
                resizedHeight,
                Image.SCALE_SMOOTH
        );

        JLabel expectedLabel = new JLabel(new ImageIcon(resizedImage));

        assertEquals(expectedLabel, testLabel);
    }
}
