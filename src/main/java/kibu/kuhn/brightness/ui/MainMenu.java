package kibu.kuhn.brightness.ui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.inject.Inject;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.google.common.eventbus.Subscribe;

import kibu.kuhn.brightness.event.HelpEvent;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class MainMenu extends MouseAdapter
{

    private JDialog dialog;
    private SettingsMenu settingsMenu;
    private HelpMenu helpMenu;
    private JTextArea errorPane;
    private MainMenuLocationHandler mainMenuLocationHandler;
    private SliderPane sliderPane;
    @Inject
    private IPreferencesService preferences;
    @Inject
    private IEventbus eventbus;

    public MainMenu() {
        init();
    }

    private void init() {
        eventbus.register(this);
    }

    private HelpMenu createHelpMenu() {
        var parent = settingsMenu == null ? null : settingsMenu.getDialog();
        var menu = new HelpMenu(parent);
        menu.setWindowCloseAction(e -> helpMenu = null);
        return menu;
    }

    private SettingsMenu createSettingsMenu() {
        var menu = new SettingsMenu();
        menu.setWindowCloseAction(e -> {
            settingsMenu = null;
            if (helpMenu != null) {
                helpMenu.setDialogVisible(false);
                helpMenu = null;
            }
        });
        return menu;
    }

    @Subscribe
    void showHelpMenu(HelpEvent e) {
        if (helpMenu == null) {
            helpMenu = createHelpMenu();
        }
        helpMenu.setDialogVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            showDialog(e);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            showSettings();
        }
    }

    private void showSettings() {
        if (settingsMenu != null) {
            return;
        }

        settingsMenu = createSettingsMenu();
        settingsMenu.setDialogVisible(true);
    }

    private void showDialog(MouseEvent e) {
        if (dialog != null) {
            return;
        }

        dialog = new JDialog((Frame) null, false);
        var close = new Close();
        dialog.addWindowFocusListener(close);
        dialog.addWindowListener(close);
        var pane = dialog.getContentPane();
        pane.setLayout(new BorderLayout());
        sliderPane = new SliderPane();
        pane.add(new JScrollPane(sliderPane), CENTER);
        errorPane = new ErrorPane();
        pane.add(errorPane, SOUTH);
        dialog.setUndecorated(true);
        dialog.setMinimumSize(new Dimension(10, 400));
        mainMenuLocationHandler = new MainMenuLocationHandler(e, dialog);
        dialog.pack();
        dialog.setVisible(true);
    }

    private class Close extends WindowAdapter
    {

        @Override
        public void windowClosing(WindowEvent e) {
            closeDialog();
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            closeDialog();
        }

        private void closeDialog() {
            if (preferences.isMainMenuLocationUpdatEnabled()) {
                mainMenuLocationHandler.saveLocation();
            }
            if (dialog == null) {
                return;
            }

            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
            mainMenuLocationHandler.unregister();
            sliderPane.unregister();
        }
    }

    void setErrorText(String text) {
        errorPane.setText(text);
        ;
    }

}
