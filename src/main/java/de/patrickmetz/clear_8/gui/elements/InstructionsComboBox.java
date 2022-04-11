package de.patrickmetz.clear_8.gui.elements;

import de.patrickmetz.clear_8.globals.Config;
import de.patrickmetz.clear_8.globals.Text;

import javax.swing.JComboBox;

final public class InstructionsComboBox extends JComboBox<Integer> {

    public InstructionsComboBox() {
        setEditable(false);
        setFocusable(false);

        setToolTipText(Text.Gui.TOOL_TIP_CPU_INSTRUCTIONS);

        for (
                int value = Config.Gui.IPS_MINIMUM;
                value     <= Config.Gui.IPS_MAXIMUM;
                value     += Config.Gui.IPS_STEP_SIZE
        ) {
            addItem(value);
        }
    }

    @Override
    public void setSelectedItem(Object value) {
        super.setSelectedItem(value);
    }

}
