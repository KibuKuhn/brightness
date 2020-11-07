package kibu.kuhn.brightness.ui.component;

import javax.inject.Inject;
import javax.swing.Action;
import javax.swing.JRadioButton;

import kibu.kuhn.brightness.ui.Icons;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class XRadioButton extends JRadioButton
{

    private static final long serialVersionUID = 1L;

    @Inject
    private Icons icons;

    public XRadioButton(Action action) {
        super(action);
        setSelectedIcon(icons.getIcon("radiobutton_checked18"));
        setIcon(icons.getIcon("radiobutton_unchecked18"));
        setRolloverEnabled(false);
    }

}
