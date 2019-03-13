/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.structure;

import javax.swing.*;
import java.awt.*;

final public class Window extends JFrame {

    private static final String APPLICATION_NAME = "clear_8";

    public Window() throws HeadlessException {
        setTitle(APPLICATION_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
}
