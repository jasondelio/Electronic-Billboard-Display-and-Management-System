import cab302.viewer.exceptions.MalformedHexadecimalColourException;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static cab302.viewer.util.HexToRGB.HexToRGB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestHexToRGB {

    @Test
    public void throwsExceptionWhenProvidedSingleDigitHexColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("#D"));
    }
    @Test
    public void throwsExceptionWhenProvidedTwoDigitHexColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("#02"));
    }
    @Test
    public void throwsExceptionWhenProvidedFourDigitHexColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("#6733"));
    }
    @Test
    public void throwsExceptionWhenProvidedFiveDigitHexColour() {

        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("#ADE45"));
    }
    @Test
    public void throwsExceptionWhenProvidedHexColourWithoutHashIdentifier() {

        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("28FC12"));
    }
    @Test
    public void throwsExceptionWhenHashIdentifierInWrongPlace() {
        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("DEF#726"));
    }
    @Test
    public void throwsExceptionWhenValuesOutOfRange() {
        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("#39JDA1"));
    }
    @Test
    public void throwsExceptionWhenValuesContainSpecialCharsOtherThanHash() {
        assertThrows(MalformedHexadecimalColourException.class, () -> HexToRGB("#12ED@1"));
    }
    @Test
    public void createValidColorObjectWhenProvidedValidSixDigitString_Simple() throws MalformedHexadecimalColourException {
        assertEquals(new Color(255,0,0), HexToRGB("#FF0000"));
    }
    @Test
    public void createValidColorObjectWhenProvidedValidSixDigitString_Complex() throws MalformedHexadecimalColourException {
        assertEquals(new Color(19,152,77), HexToRGB("#13984D"));
    }
    @Test
    public void createValidColorObjectWhenProvidedValidThreeDigitString_Simple() throws MalformedHexadecimalColourException {
        assertEquals(new Color(255,255,255), HexToRGB("#FFF"));
    }
    @Test
    public void createValidColorObjectWhenProvidedValidThreeDigitString_Complex() throws MalformedHexadecimalColourException {
        assertEquals(new Color(221,153,170), HexToRGB("#D9A"));
    }
}

