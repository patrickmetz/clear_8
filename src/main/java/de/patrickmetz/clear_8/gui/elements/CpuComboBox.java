package de.patrickmetz.clear_8.gui.elements;

import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JComboBox;

final public class CpuComboBox extends JComboBox<String> {

    public CpuComboBox() {
        setEditable(false);
        setFocusable(false);

        setToolTipText(Text.Gui.TOOL_TIP_CPU);

        addItem(Text.Gui.CPU_SCHIP);
        addItem(Text.Gui.CPU_VIP);
    }

    public void setSelectedItem(Object value) {
        super.setSelectedItem(
                (boolean) value ? Text.Gui.CPU_VIP : Text.Gui.CPU_SCHIP
        );
    }

}
