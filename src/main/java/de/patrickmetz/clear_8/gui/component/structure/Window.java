/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 14:00.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.structure;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private static final String APPLICATION_NAME = "clear_8";

    public Window() throws HeadlessException {
        setTitle(APPLICATION_NAME);
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
