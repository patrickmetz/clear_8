package de.patrickmetz.clear_8.gui.structure;

import de.patrickmetz.clear_8.globals.Config;

import javax.swing.JPanel;
import java.awt.FlowLayout;

final public class BottomPanel extends JPanel {

    public BottomPanel() {
        setLayout(new FlowLayout(
                FlowLayout.LEFT,
                Config.Gui.BOTTOM_PANEL_HORIZONTAL_GAP,
                Config.Gui.BOTTOM_PANEL_VERTICAL_GAP)
        );
    }
}
