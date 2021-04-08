package kibu.kuhn.brightness.ui;

import java.awt.Component;
import java.util.Locale;

import javax.inject.Inject;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

import kibu.kuhn.brightness.utils.Injection;

@Injection
public class LocaleRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = 1L;

    @Inject
    private Icons icons;

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        var locale = (Locale) value;
        var component = super.getListCellRendererComponent(list, locale.getDisplayLanguage(), index, isSelected,
                cellHasFocus);
        var icon = getIcon(locale);
        setIcon(icon);
        return component;
    }

    private Icon getIcon(Locale locale) {
        var iconname = locale.getLanguage() + 25;
        return icons.getIcon(iconname);
    }

}
