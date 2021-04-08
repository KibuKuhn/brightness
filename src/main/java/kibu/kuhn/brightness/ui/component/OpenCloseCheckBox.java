package kibu.kuhn.brightness.ui.component;

import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.JCheckBox;

import kibu.kuhn.brightness.ui.Icons;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class OpenCloseCheckBox extends JCheckBox
{

    private static final long serialVersionUID = 1L;

    @Inject
    private Icons icons;

    public OpenCloseCheckBox(String text) {
        super(text);
        setContentAreaFilled(false);
        setIcon(icons.getIcon("right18"));
    }

    @Override
    protected void fireActionPerformed(ActionEvent event) {
        setIcon(isSelected() ? icons.getIcon("down18") : icons.getIcon("right18"));
        super.fireActionPerformed(event);
    }
}
