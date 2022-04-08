package de.patrickmetz.clear_8.gui.component.structure;

import javax.swing.*;
import java.awt.*;

final public class CenterPanel extends JPanel {

    public CenterPanel() {
        setPreferredSize(new Dimension(640, 480));
        setLayout(new GridBagLayout());
    }
}
