/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 19:30.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {

    public BottomPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        setMinimumSize(new Dimension(6, 48));
    }
}
