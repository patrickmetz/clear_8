package de.patrickmetz.clear_8.gui.output;

import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JTextPane;

final public class StatusPane extends JTextPane {

    private String fileName;
    private String fps;

    public StatusPane() {
        setEditable(false);
        setOpaque(false);
        setFocusable(false);
    }

    public void updateFileName(String fileName) {
        if ((fileName != null) && containsDot(fileName)) {
            this.fileName = lowercaseWithoutExtension(fileName);
        } else {
            this.fileName = null;
        }

        updateText();
    }

    public void updateFps(String fps) {
        this.fps = fps;
        updateText();
    }

    private boolean containsDot(String fileName) {
        return fileName.indexOf('.') > -1;
    }

    private String lowercaseWithoutExtension(String fileName) {
        return fileName.split("\\.")[0].toLowerCase();
    }

    private void updateText() {
        String text = "";

        if (fps != null) {
            if (fileName != null) {
                text += " | " + fps + Text.Gui.FPS;
            } else {
                text += fps + " " + Text.Gui.FPS;
            }

        }

        setText(text);
    }

}
