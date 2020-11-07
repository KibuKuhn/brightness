package kibu.kuhn.brightness.ui.component;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import kibu.kuhn.brightness.ui.Icons;

public class OpenCloseCheckBox extends JCheckBox
{

    private static final long serialVersionUID = 1L;

    public OpenCloseCheckBox(String text) {
        super(text);
        setContentAreaFilled(false);
        setIcon(Icons.getIcon("right18"));
    }

    @Override
    protected void fireActionPerformed(ActionEvent event) {
        setIcon(isSelected() ? Icons.getIcon("down18") : Icons.getIcon("right18"));
        super.fireActionPerformed(event);
    }
}
