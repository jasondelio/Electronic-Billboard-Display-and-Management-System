package cab302.viewer.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {
    /**
     * Create a new CloseAction for use with the Billboard Viewer
     * @param text Name of the action
     * @param desc Description of action done
     * @param mnemonicKey The key to bind the action to
     */
    public CloseAction(String text, String desc, Integer mnemonicKey) {

        super(text);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonicKey);
    }
    /**
     * The action's effect on the application
     * @param e Action event. Unused.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (Frame frame : JFrame.getFrames()) {
            frame.dispose();
            RunViewer run = new RunViewer();
            run.time.cancel();
        }
    }
}