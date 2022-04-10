package de.patrickmetz.clear_8.gui.elements;

import javax.swing.*;

final public class StopButton extends JButton {

    private static final String TEXT_STOP = "Stop";

    public StopButton() {
        setEnabled(false);
        setFocusable(false);

        setText(TEXT_STOP);
    }

}
