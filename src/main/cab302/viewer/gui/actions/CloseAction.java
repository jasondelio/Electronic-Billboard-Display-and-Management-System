package cab302.viewer.gui.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {

    public CloseAction(String text, String desc, Integer mnemonicKey) {

        super(text);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonicKey);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Frame frame : JFrame.getFrames()) {
            frame.dispose();
        }
    }
}
