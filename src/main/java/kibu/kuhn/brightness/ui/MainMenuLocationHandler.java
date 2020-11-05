package kibu.kuhn.brightness.ui;

import java.awt.Point;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import kibu.kuhn.brightness.event.EventBusSupport;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.event.MainMenuPositionEvent;
import kibu.kuhn.brightness.prefs.IPreferencesService;

class MainMenuLocationHandler implements EventBusSupport
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuLocationHandler.class);

    private JDialog dialog;

    private MouseEvent mouseEvent;

    MainMenuLocationHandler(MouseEvent e, JDialog dialog) {
        this.mouseEvent = e;
        this.dialog = dialog;
        IEventbus.get().register(this);
        initLocation();
    }

    @Subscribe
    void mainMenuPositionChanged(MainMenuPositionEvent e) {
        if (IPreferencesService.get().isMainMenuLocationUpdatEnabled()) {
            var delegate = e.getDelegate();
            var locationOnScreen = delegate.getLocationOnScreen();
            LOGGER.debug("locationOnScreen: {}", locationOnScreen);
            dialog.setLocation(locationOnScreen);
        }
    }

    private void initLocation() {
        Point locationOnScreen = null;
        if (!IPreferencesService.get().isMainMenuLocationUpdatEnabled()) {
            var trayIcon = (TrayIcon) mouseEvent.getSource();
            locationOnScreen = mouseEvent.getLocationOnScreen();
            var size = trayIcon.getSize();
            if (IPreferencesService.get().getMainMenuLocation() == null) {
                locationOnScreen.y = locationOnScreen.y + size.height;
                locationOnScreen.x = locationOnScreen.x - size.width / 2;
            } else {
                locationOnScreen = IPreferencesService.get().getMainMenuLocation();
            }
        }

        if (locationOnScreen == null) {
            dialog.setLocationRelativeTo(null);
        } else {
            dialog.setLocation(locationOnScreen);
        }
    }

    void saveLocation() {
        IPreferencesService.get().saveMainMenuLocation(dialog.getLocationOnScreen());
    }

    @Override
    public void unregister() {
        IEventbus.get().unregister(this);

    }
}
