package kibu.kuhn.brightness.ui;

import java.awt.Point;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;

import javax.inject.Inject;
import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import kibu.kuhn.brightness.event.EventBusSupport;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.event.MainMenuPositionEvent;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class MainMenuLocationHandler implements EventBusSupport
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuLocationHandler.class);

    private JDialog dialog;
    private MouseEvent mouseEvent;
    @Inject
    private IEventbus eventbus;
    @Inject
    private IPreferencesService preferences;

    public MainMenuLocationHandler(MouseEvent e, JDialog dialog) {
        this.mouseEvent = e;
        this.dialog = dialog;
        eventbus.register(this);
        initLocation();
    }

    @Subscribe
    void mainMenuPositionChanged(MainMenuPositionEvent e) {
        if (preferences.isMainMenuLocationUpdatEnabled()) {
            var delegate = e.getDelegate();
            var locationOnScreen = delegate.getLocationOnScreen();
            LOGGER.debug("locationOnScreen: {}", locationOnScreen);
            dialog.setLocation(locationOnScreen);
        }
    }

    private void initLocation() {
        Point locationOnScreen = null;
        if (!preferences.isMainMenuLocationUpdatEnabled()) {
            var trayIcon = (TrayIcon) mouseEvent.getSource();
            locationOnScreen = mouseEvent.getLocationOnScreen();
            var size = trayIcon.getSize();
            if (preferences.getMainMenuLocation() == null) {
                locationOnScreen.y = locationOnScreen.y + size.height;
                locationOnScreen.x = locationOnScreen.x - size.width / 2;
            } else {
                locationOnScreen = preferences.getMainMenuLocation();
            }
        }

        if (locationOnScreen == null) {
            dialog.setLocationRelativeTo(null);
        } else {
            dialog.setLocation(locationOnScreen);
        }
    }

    void saveLocation() {
        preferences.saveMainMenuLocation(dialog.getLocationOnScreen());
    }

    @Override
    public void unregister() {
        eventbus.unregister(this);

    }
}
