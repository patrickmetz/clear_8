package de.patrickmetz.clear_8.gui.structure;

import de.patrickmetz.clear_8.globals.Config;

import javax.swing.JPanel;
import java.awt.BorderLayout;

final public class WindowContent extends JPanel {

    public WindowContent() {
        setLayout(new BorderLayout(
                Config.Gui.WINDOW_HORIZONTAL_GAP,
                Config.Gui.WINDOW_VERTICAL_GAP)
        );
    }
}
