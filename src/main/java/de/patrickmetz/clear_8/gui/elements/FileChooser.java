package de.patrickmetz.clear_8.gui.elements;

import javax.swing.JFileChooser;
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
