package kibu.kuhn.brightness.ui.component;

import static java.awt.Color.LIGHT_GRAY;

import java.awt.Graphics;

import javax.inject.Inject;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import kibu.kuhn.brightness.ui.Icons;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class XCheckBox extends JCheckBox
{

    private static final long serialVersionUID = 1L;

    @Inject
    private Icons icons;

    public XCheckBox(String name) {
        super(name);
        init();
    }

    public XCheckBox(Action action) {
        super(action);
        init();
    }

    private void init() {
        setBorderPainted(false);
        setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));
        setSelectedIcon(icons.getIcon("checkbox_selected18"));
        setIcon(icons.getIcon("checkbox_unselected18"));
        setRolloverEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasFocus()) {
            getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
        }
    }

}
