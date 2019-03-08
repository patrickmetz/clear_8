/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 13:36.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel {

    public Screen() {
        setPreferredSize(new Dimension(640, 480));
        setLayout(new GridBagLayout());
    }
}
