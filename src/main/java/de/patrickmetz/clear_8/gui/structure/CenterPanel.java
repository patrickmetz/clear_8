package de.patrickmetz.clear_8.gui.structure;

import de.patrickmetz.clear_8.globals.Config;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagLayout;

final public class CenterPanel extends JPanel {

    public CenterPanel() {
        setPreferredSize(new Dimension(
                Config.Gui.CENTER_PANEL_WIDTH,
                Config.Gui.CENTER_PANEL_HEIGHT
        ));

        setLayout(new GridBagLayout());
    }
}
