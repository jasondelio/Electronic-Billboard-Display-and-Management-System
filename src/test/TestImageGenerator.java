import cab302.viewer.exceptions.BadImageFormatException;
import cab302.viewer.gui.ImageGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestImageGenerator {

    @Test
    public void throwsExceptionWhenProvidedInvalidString_Download() {
        assertThrows(BadImageFormatException.class, () -> new ImageGenerator().downloadImage("Not an image string"));
    }
    @Test
    public void throwsExceptionWhenProvidedInvalidString_Decode() {
        assertThrows(BadImageFormatException.class, () -> new ImageGenerator().decodeDataString("rauhgubauadna avdfivyadf i ay gab haubdv :AS ]a"));
    }
    @Test
    public void throwsExceptionWhenProvidedInvalidString_Check() {
        assertThrows(BadImageFormatException.class, () -> new ImageGenerator().isBase64EncodedImage("Not an image string"));
    }
    @Test
    public void throwsExceptionWhenTryingToUseDataStringToDownloadImage() {
        assertThrows(BadImageFormatException.class, () -> new ImageGenerator().downloadImage("iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwODo0ODo0MCArMDkwMI5631cAAAAHdElNRQfkAxgXOwi4DCRrAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAMJJREFUeNrtVdkKgDAMq8P//+UJVmrMrm4DDzAPUstYesQocgNijPhUWJye6UJIU8sOiwcubRBQ4UqmTEb8sg6oOuzgIyDlYL4UiFtRxw5w7sSk0xseWsAmjIwEM7OSQOUbR+W1C2tvgXpYKTF+DGchcv2s6rFlSBrFbjxCzBpf0wTbXuTfUPbkmpkaGJHMaTRPYNP0Xz3viXPwuJDAPtC7+gjSoE7vwUVFTRcaUBfL1OlCqX15CUognXzwr/fjx4PYACcSBKYdfe/1AAAAAElFTkSuQmCC"));
    }
    @Test
    public void throwsExceptionWhenTryingToDecodeURL() {
        assertThrows(BadImageFormatException.class, () -> new ImageGenerator().decodeDataString("https://cloudstor.aarnet.edu.au/plus/s/5fhToroJL0nMKvB/download"));
    }
    @Test
    public void doesNotThrowOnCreateBufferedImage_download() {
        assertDoesNotThrow(
                () -> new ImageGenerator().downloadImage("https://cloudstor.aarnet.edu.au/plus/s/5fhToroJL0nMKvB/download")
        );
    }
    @Test
    public void doesNotThrowOnCreateBufferedImage_decode() {
        assertDoesNotThrow(
                () -> new ImageGenerator().decodeDataString("iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwODo0ODo0MCArMDkwMI5631cAAAAHdElNRQfkAxgXOwi4DCRrAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAMJJREFUeNrtVdkKgDAMq8P//+UJVmrMrm4DDzAPUstYesQocgNijPhUWJye6UJIU8sOiwcubRBQ4UqmTEb8sg6oOuzgIyDlYL4UiFtRxw5w7sSk0xseWsAmjIwEM7OSQOUbR+W1C2tvgXpYKTF+DGchcv2s6rFlSBrFbjxCzBpf0wTbXuTfUPbkmpkaGJHMaTRPYNP0Xz3viXPwuJDAPtC7+gjSoE7vwUVFTRcaUBfL1OlCqX15CUognXzwr/fjx4PYACcSBKYdfe/1AAAAAElFTkSuQmCC")
        );
    }
}
