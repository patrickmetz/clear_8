package de.patrickmetz.clear_8.gui.listeners;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.gui.elements.FileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

final public class LoadRomButtonListener implements ActionListener {

    private final FileChooser fileChooser;
    private final Emulator    emulator;

    public LoadRomButtonListener(Emulator emulator, FileChooser fileChooser) {
        this.emulator = emulator;
        this.fileChooser = fileChooser;
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        File file = fileChooser.getFile();

        if (file != null) {
            emulator.stop();
            emulator.setGamePath(file.getPath());
            emulator.start();
        }
    }
}
