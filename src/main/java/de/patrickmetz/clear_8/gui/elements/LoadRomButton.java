package de.patrickmetz.clear_8.gui.elements;

import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JButton;

final public class LoadRomButton extends JButton {

    public LoadRomButton() {
        setEnabled(true);
        setText(Text.Gui.LOAD_ROM);
        setFocusable(false);
    }

}
