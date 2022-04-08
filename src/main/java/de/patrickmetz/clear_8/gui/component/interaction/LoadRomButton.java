package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;

final public class LoadRomButton extends JButton {

    private static final String TEXT_LOAD_ROM = "Load ROM";

    public LoadRomButton() {
        setEnabled(true);
        setText(TEXT_LOAD_ROM);

        setFocusable(false);
    }

}
