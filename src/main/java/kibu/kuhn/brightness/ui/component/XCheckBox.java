package kibu.kuhn.brightness.ui.component;

import static java.awt.Color.LIGHT_GRAY;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import kibu.kuhn.brightness.ui.Icons;

public class XCheckBox extends JCheckBox
{

    private static final long serialVersionUID = 1L;

    public XCheckBox(String text) {
        super(text);
        setBorderPainted(false);
        setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));
        setSelectedIcon(Icons.getIcon("checkbox_selected18"));
        setIcon(Icons.getIcon("checkbox_unselected18"));
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
