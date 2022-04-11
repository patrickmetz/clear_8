package de.patrickmetz.clear_8.gui.elements;

import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JButton;

final public class StopButton extends JButton {

    public StopButton() {
        setEnabled(false);
        setFocusable(false);

        setText(Text.Gui.STATE_STOP);
    }

}
