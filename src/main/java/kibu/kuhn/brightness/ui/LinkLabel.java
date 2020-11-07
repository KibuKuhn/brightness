package kibu.kuhn.brightness.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JLabel;

import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class LinkLabel extends JLabel
{
    private static final long serialVersionUID = 1L;
    static final int WIDTH = 10;

    @Inject
    private IPreferencesService preferences;

    public LinkLabel() {
        setText(preferences.isAllUnits() ? "&" : "");
        setForeground(Color.GREEN);
    }

    @Override
    public Dimension getMaximumSize() {
        return getFixedSize(super.getMaximumSize());
    }

    private Dimension getFixedSize(Dimension size) {
        size.width = WIDTH;
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return getFixedSize(super.getMinimumSize());
    }

    @Override
    public Dimension getPreferredSize() {
        return getFixedSize(super.getPreferredSize());
    }

}
