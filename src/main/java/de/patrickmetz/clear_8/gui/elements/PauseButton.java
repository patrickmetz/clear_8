package de.patrickmetz.clear_8.gui.elements;

import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JButton;

final public class PauseButton extends JButton {

    public PauseButton() {
        setEnabled(false);
        setFocusable(false);

        usePauseText();
    }

    public void usePauseText() {
        setText(Text.Gui.STATE_PAUSE);
    }

    public void useResumeText() {
        setText(Text.Gui.STATE_RESUME);
    }

}
