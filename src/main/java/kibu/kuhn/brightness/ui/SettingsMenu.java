package kibu.kuhn.brightness.ui;

import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.RELATIVE;
import static java.awt.GridBagConstraints.REMAINDER;
import static java.awt.GridBagConstraints.WEST;
import static javax.swing.Box.createGlue;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.event.AllUnitsEvent;
import kibu.kuhn.brightness.event.HelpEvent;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.ui.component.OpenCloseCheckBox;
import kibu.kuhn.brightness.ui.component.XButton;
import kibu.kuhn.brightness.ui.component.XCheckBox;
import kibu.kuhn.brightness.utils.IExitHandler;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class SettingsMenu
{

    private static final int HEIGHT = 450;
    private static final int WIDTH = 400;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsMenu.class);

    private JDialog dialog;
    private JComboBox<LookAndFeelInfo> lafs;
    private JComboBox<Locale> locales;
    private JLabel infoLabel;
    private LocaleAction messageAction;
    private Consumer<? super ComponentEvent> windowCloseAction;
    private ColorTempPane colorTempPane;
    @Inject
    private IPreferencesService preferences;
    @Inject
    private Icons icons;
    @Inject
    private I18n i18n;
    @Inject
    private IEventbus eventbus;
    @Inject
    private IExitHandler exitHandler;

    public SettingsMenu() {
        init();
    }

    void setDialogVisible(boolean visible) {
        if (dialog == null) {
            return;
        }
        if (!visible) {
            dialog.setVisible(false);
            return;
        }

        messageAction.setEnabled(false);
        LookAndFeelInfo laf = preferences.getLaf();
        lafs.setSelectedItem(laf);
        Locale locale = preferences.getLocale();
        locales.setSelectedItem(locale);
        messageAction.setEnabled(true);
        dialog.setVisible(visible);
    }

    private void init() {
        dialog = new JDialog(null, i18n.get("settingsmenu.title"), APPLICATION_MODAL);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doClose(e);
            };
        });

        // l&f
        dialog.setIconImage(icons.getImage("brightness36_filled"));
        var pane = dialog.getContentPane();
        pane.setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 2, 2, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = NONE;
        constraints.anchor = WEST;
        pane.add(new JLabel(i18n.get("settingsmenu.laf")), constraints);

        constraints.insets.right = 2;
        constraints.anchor = EAST;
        constraints.gridx = RELATIVE;
        constraints.gridwidth = REMAINDER;
        constraints.weightx = 1;
        constraints.fill = HORIZONTAL;
        lafs = new JComboBox<>();
        var preferredSize = new Dimension(0, 28);
        lafs.setPreferredSize(preferredSize);
        lafs.setMinimumSize(preferredSize);
        lafs.setRenderer(new LafRenderer());
        lafs.setModel(createLafModel());
        lafs.addActionListener(new LafAction());
        pane.add(lafs, constraints);

        // locale
        constraints.insets.right = 10;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.fill = NONE;
        pane.add(new JLabel(i18n.get("settingsmenu.locale")), constraints);

        constraints.insets.right = 2;
        constraints.anchor = EAST;
        constraints.gridx = RELATIVE;
        constraints.gridwidth = REMAINDER;
        constraints.weightx = 1;
        constraints.fill = HORIZONTAL;
        locales = new JComboBox<>();
        locales.setPreferredSize(preferredSize);
        locales.setMinimumSize(preferredSize);
        locales.setRenderer(new LocaleRenderer());
        locales.setModel(createLocalesModel());
        messageAction = new LocaleAction();
        locales.addActionListener(messageAction);
        pane.add(locales, constraints);

        // dark mode
        constraints.insets.top = 2;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = 1;
        constraints.fill = NONE;
        var darkMode = new XCheckBox(i18n.get("settingsmenu.darkmode"));
        darkMode.setSelected(preferences.isDarkMode());
        darkMode.addActionListener(new DarkModeAction());
        pane.add(darkMode, constraints);

        // adjust
        constraints.insets.top = 2;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = 1;
        constraints.fill = NONE;
        var adjustLocation = new XCheckBox(i18n.get("settingsmenu.adjust.mainmenu.location"));
        adjustLocation.setSelected(preferences.isMainMenuLocationUpdatEnabled());
        adjustLocation.addActionListener(new AdjustLocationAction());
        pane.add(adjustLocation, constraints);

        // all units
        constraints.insets.top = 2;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = 1;
        constraints.fill = NONE;
        var allUnits = new XCheckBox(i18n.get("settingsmenu.allunits"));
        allUnits.setSelected(preferences.isAllUnits());
        allUnits.addActionListener(new AllUnitsAction());
        pane.add(allUnits, constraints);

        // color temp
        var colorTemp = new OpenCloseCheckBox(i18n.get("settingsmenu.colortemp"));
        colorTemp.addActionListener(new ColorTempAction());
        pane.add(colorTemp, constraints);
        colorTempPane = new ColorTempPane();
        colorTempPane.setVisible(false);
        constraints.fill = HORIZONTAL;
        pane.add(colorTempPane, constraints);

        // help
        constraints.insets.top = 2;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = 1;
        constraints.fill = NONE;
        var help = new XButton(new HelpAction(event -> eventbus.post(new HelpEvent())));
        pane.add(help, constraints);

        // Glue
        constraints.insets.top = 2;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = 1;
        constraints.fill = BOTH;
        pane.add(createGlue(), constraints);

        // exit
        constraints.insets.top = 2;
        constraints.anchor = WEST;
        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = 1;
        constraints.fill = NONE;
        pane.add(new XButton(new ExitAction(event -> exitHandler.exit())), constraints);

        constraints.gridx = 0;
        constraints.gridy = RELATIVE;
        constraints.gridheight = 1;
        constraints.gridwidth = REMAINDER;
        constraints.weighty = 0;
        constraints.weightx = 1;
        constraints.fill = HORIZONTAL;
        infoLabel = new InfoLabel();
        infoLabel.setMinimumSize(preferredSize);
        infoLabel.setMaximumSize(preferredSize);
        infoLabel.setPreferredSize(preferredSize);
        pane.add(infoLabel, constraints);

        dialog.pack();
        dialog.setSize(WIDTH, HEIGHT);
        dialog.setLocationRelativeTo(null);
    }

    private void saveSettings() {
        colorTempPane.save();
    }

    private ComboBoxModel<Locale> createLocalesModel() {
        DefaultComboBoxModel<Locale> model = new DefaultComboBoxModel<>();
        model.addElement(Locale.GERMAN);
        model.addElement(Locale.ENGLISH);
        return model;
    }

    private ComboBoxModel<LookAndFeelInfo> createLafModel() {
        DefaultComboBoxModel<LookAndFeelInfo> model = new DefaultComboBoxModel<>();
    //@formatter:off
    Arrays.stream(UIManager.getInstalledLookAndFeels())
          .sorted((l1, l2) -> l1.getName().compareTo(l2.getName()))
          .forEach(model::addElement);
    //@formatter:on
        return model;
    }

    private void doClose(WindowEvent e) {
        dialog.dispose();
        dialog = null;
        saveSettings();
        if (windowCloseAction == null) {
            return;
        }
        windowCloseAction.accept(e);
    }

    public void showMessage(String message) {
        infoLabel.setText(message);
    }

    private class LocaleAction extends AbstractAction
    {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isEnabled()) {
                return;
            }

            preferences.saveLocale((Locale) locales.getSelectedItem());
            showMessage(i18n.get("settingsmenu.message"));
        }
    }

    private class LafAction extends AbstractAction
    {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            var laf = (LookAndFeelInfo) lafs.getSelectedItem();
            try {
                UIManager.setLookAndFeel(laf.getClassName());
                SwingUtilities.updateComponentTreeUI(dialog.getRootPane());
                preferences.saveLaf(laf);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
        }
    }

    void setWindowCloseAction(Consumer<? super ComponentEvent> c) {
        this.windowCloseAction = c;
    }

    private class AdjustLocationAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            preferences.setMainMenuLocationUpdateEnabled(((JCheckBox) e.getSource()).isSelected());
        }
    }

    private class DarkModeAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            preferences.setDarkMode(((JCheckBox) e.getSource()).isSelected());
            icons.clearCache();
        }
    }

    private class AllUnitsAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean selected = ((JCheckBox) e.getSource()).isSelected();
            preferences.setAllUnits(selected);
            eventbus.post(new AllUnitsEvent(selected));
        }
    }

    private class ColorTempAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            var checkBox = (JCheckBox) e.getSource();
            colorTempPane.setVisible(checkBox.isSelected());
        }

    }

    JDialog getDialog() {
        return dialog;
    }
}
