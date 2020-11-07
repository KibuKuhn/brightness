package kibu.kuhn.brightness.ui;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import kibu.kuhn.brightness.prefs.IPreferencesService;

public class IconsFactory implements Icons
{
    private Map<String, ImageIcon> icons = new HashMap<>();

    @Inject
    private IPreferencesService preferencesService;

    @Override
    public Image getImage(String iconName) {
        return getImageIcon(iconName).getImage();
    }

    @Override
    public Icon getIcon(String iconName) {
        return getImageIcon(iconName);
    }

    @Override
    public void clearCache() {
        icons = new HashMap<>();
    }

    private ImageIcon getImageIcon(final String iconName) {
        var imageIcon = icons.get(iconName);
        if (imageIcon == null) {
            var name = iconName;
            if (preferencesService.isDarkMode()) {
                name = iconName + "-inv";
            }
            var resource = getClass().getResource("/" + name + ".png");
            if (resource == null && preferencesService.isDarkMode()) {
                name = iconName;
                resource = getClass().getResource("/" + name + ".png");
            }
            imageIcon = new ImageIcon(resource, name);
            icons.put(iconName, imageIcon);
        }
        return imageIcon;
    }
}
