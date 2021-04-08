package kibu.kuhn.brightness.ui;

import java.awt.Image;

import javax.swing.Icon;

public interface Icons
{

    Image getImage(String iconName);

    Icon getIcon(String iconName);

    void clearCache();

}
