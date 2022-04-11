package de.patrickmetz.clear_8.gui.structure;

import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JFrame;
import java.awt.HeadlessException;

final public class Window extends JFrame {

    public Window() throws HeadlessException {
        setTitle(Text.Gui.APPLICATION_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
}
