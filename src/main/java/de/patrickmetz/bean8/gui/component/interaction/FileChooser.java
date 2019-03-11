/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 13:32.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooser extends JFileChooser {

    private static final String FILE_EXTENSION = "ch8";
    private static final String FILE_EXTENSION_DESCRIPTION = "chip 8 ROMs (.ch8)";

    public FileChooser() {
        setFocusable(false);

        setFileFilter(
                new FileNameExtensionFilter(
                        FILE_EXTENSION_DESCRIPTION,
                        FILE_EXTENSION
                )
        );
    }

    public File getFile() {
        File file = null;

        if (showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = getSelectedFile();
        }

        return file;
    }

}
