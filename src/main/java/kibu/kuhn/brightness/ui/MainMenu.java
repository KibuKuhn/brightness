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

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kibu.kuhn.brightness.prefs.IPreferencesService;

class MainMenu extends MouseAdapter
{

    private JDialog dialog;
    private ConfigMenu configMenu;
    private SettingsMenu settingsMenu;
    private HelpMenu helpMenu;
    OpenItemHandler openItemHandler = new OpenItemHandler(this);

//  private Consumer<? super ActionEvent> buttonbarAction = ae -> {
//    switch (ae.getActionCommand()) {
//      case ButtonBar.ACTION_CONFIG:
//        if (configMenu == null) {
//          configMenu = createConfigMenu();
//        }
//        configMenu.setDialogVisible(true);
//        break;
//      case ButtonBar.ACTION_EXIT:
//        System.exit(0);
//        break;
//      case ButtonBar.ACTION_SETTINGS:
//        if (settingsMenu == null) {
//          settingsMenu = createSettingsMenu();
//        }
//        settingsMenu.setDialogVisible(true);
//        break;
//      case ButtonBar.ACTION_HELP:
//        if (helpMenu == null) {
//          helpMenu = createHelpMenu();
//        }
//        helpMenu.setDialogVisible(true);
//        break;
//    }

//  };

    private JTextArea errorPane;
    private MainMenuLocationHandler mainMenuLocationHandler;

    MainMenu() {
    }

    private HelpMenu createHelpMenu() {
        var menu = new HelpMenu();
        menu.setWindowCloseAction(e -> helpMenu = null);
        return menu;
    }

    private SettingsMenu createSettingsMenu() {
        var menu = new SettingsMenu();
        menu.setWindowCloseAction(e -> settingsMenu = null);
        return menu;
    }

    private ConfigMenu createConfigMenu() {
        var menu = new ConfigMenu();
        menu.setWindowCloseAction(e -> configMenu = null);
        return menu;
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

        SettingsMenu menu = createSettingsMenu();
        menu.setDialogVisible(true);
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
        var brightnessPane = new SliderPane();
        pane.add(new JScrollPane(brightnessPane), CENTER);
        errorPane = new ErrorPane();
        pane.add(errorPane, SOUTH);
        dialog.setUndecorated(true);
        dialog.setMinimumSize(new Dimension(10, 400));

        mainMenuLocationHandler = new MainMenuLocationHandler(e, dialog);
        mainMenuLocationHandler.initLocation();

        dialog.pack();
        dialog.setVisible(true);
    }

    private void closeDialog() {
        if (IPreferencesService.get().isMainMenuLocationUpdatEnabled()) {
            mainMenuLocationHandler.saveLocation();
        }
        if (dialog == null) {
            return;
        }

        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
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
    }

    void setErrorText(String text) {
        errorPane.setText(text);
        ;
    }
}
