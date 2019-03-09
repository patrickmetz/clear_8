/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:33.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.structure;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private static final String APPLICATION_TITLE = "clear8";

    public Window() throws HeadlessException {
        setTitle(APPLICATION_TITLE);
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
