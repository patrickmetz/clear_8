package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

final public class FileChooser extends JFileChooser {

    public FileChooser() {
        setFocusable(false);
    }

    public File getFile() {
        File file = null;

        if (showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = getSelectedFile();
        }

        return file;
    }

}
