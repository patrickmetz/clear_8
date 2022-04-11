package de.patrickmetz.clear_8.gui.structure;

import de.patrickmetz.clear_8.globals.Config;

import javax.swing.JPanel;
import java.awt.FlowLayout;

final public class TopPanel extends JPanel {

    public TopPanel() {
        setLayout(new FlowLayout(
                FlowLayout.LEFT,
                Config.Gui.TOP_PANEL_HORIZONTAL_GAP,
                Config.Gui.TOP_PANEL_VERTICAL_GAP
        ));
    }

}
