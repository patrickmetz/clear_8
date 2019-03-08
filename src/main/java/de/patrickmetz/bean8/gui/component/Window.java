/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 14:27.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(String title) throws HeadlessException {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    @Override
    public void pack() {
        super.pack();

        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }
}
