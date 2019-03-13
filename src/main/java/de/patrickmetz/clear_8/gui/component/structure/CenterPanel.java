/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.structure;

import javax.swing.*;
import java.awt.*;

final public class CenterPanel extends JPanel {

    public CenterPanel() {
        setPreferredSize(new Dimension(640, 480));
        setLayout(new GridBagLayout());
    }
}
