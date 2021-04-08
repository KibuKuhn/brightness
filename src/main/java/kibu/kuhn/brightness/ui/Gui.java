package kibu.kuhn.brightness.ui;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.inject.Inject;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.event.XEventQueue;
import kibu.kuhn.brightness.prefs.IPreferencesService;

public class Gui implements IGui
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Gui.class);

    @Inject
    private Icons icons;
    @Inject
    private IPreferencesService preferencesService;
    @Inject
    private I18n i18n;

    private TrayIcon trayIcon;

    private void configure(TrayIcon trayIcon) {
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(i18n.get("trayicon.tooltip"));
        trayIcon.addMouseListener(new MainMenu());
    }

    private TrayIcon createTrayIcon() {
        trayIcon = new TrayIcon(icons.getImage("brightness36"));
        configure(trayIcon);
        return trayIcon;
    }

    @Override
    public void init() {
        try {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(new XEventQueue(this::handleEventQueueException));
            var laf = preferencesService.getLaf();
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
        var tray = SystemTray.getSystemTray();
        var trayIcon = createTrayIcon();

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            LOGGER.error("TrayIcon could not be added.");
            return;
        }
    }

    @Override
    public boolean checkSupport() {
        if (!SystemTray.isSupported()) {
            LOGGER.error("SystemTray is not supported");
            return false;
        }

        if (!Desktop.isDesktopSupported()) {
            LOGGER.error("Desktop is not supported");
            return false;
        }

        return true;
    }

    private void handleEventQueueException(Throwable th) {
        trayIcon.setImage(icons.getImage("error36"));
        String message = i18n.get("trayicon.tooltip.error");
        trayIcon.setToolTip(message);
        trayIcon.displayMessage(i18n.get("trayicon.error.caption"),
                message + String.format(i18n.get("trayicon.error.ex"), th.getLocalizedMessage()), MessageType.ERROR);
        trayIcon.addMouseListener(new ExitHandler());
    }

    private static class ExitHandler extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getModifiersEx() == MouseEvent.CTRL_DOWN_MASK) {
                System.exit(0);
            }
        }

    }
}
