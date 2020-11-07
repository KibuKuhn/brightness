package kibu.kuhn.brightness.ui;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.REMAINDER;
import static java.awt.GridBagConstraints.WEST;
import static java.awt.event.ActionEvent.ACTION_PERFORMED;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import kibu.kuhn.brightness.domain.ColorTemp;
import kibu.kuhn.brightness.event.ColorTempEvent;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.ui.component.XCheckBox;
import kibu.kuhn.brightness.ui.component.XRadioButton;

class ColorTempPane extends JPanel
{

    private static final long serialVersionUID = 1L;

    private JLabel colorLabel;
    private JSpinner colorTempSteps;
    private JSpinner fromTime;
    private JSpinner toTime;

    private XRadioButton manualMode;

    private XRadioButton autoMode;

    private XCheckBox nightModeCheckbox;

    ColorTempPane() {
        initUI();
        init();

    }

    private void init() {
        boolean mode = IPreferencesService.get().isColorTemp();
        boolean currentMode = nightModeCheckbox.isSelected();
        if (mode == currentMode) {
            nightModeCheckbox.getAction().actionPerformed(new ActionEvent(nightModeCheckbox, ACTION_PERFORMED, ""));
        } else {
            nightModeCheckbox.setSelected(mode);
        }
        int kelvin = IPreferencesService.get().getColorTempKelvin();
        colorTempSteps.getModel().setValue(kelvin);

        boolean autoMode = IPreferencesService.get().isColorTempAutoMode();
        if (autoMode) {
            this.autoMode.setSelected(true);
        } else {
            manualMode.setSelected(true);
        }

    }

    private void initUI() {
        setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();
        constraints.insets.left = 20;
        constraints.insets.bottom = 10;
        // color temp steps
        constraints.anchor = WEST;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = NONE;
        constraints.gridwidth = REMAINDER;
        nightModeCheckbox = new XCheckBox(new NightModeAction());
        add(nightModeCheckbox, constraints);

        constraints.weightx = 0;
        constraints.gridwidth = 1;
        add(new JLabel("Kelvin"), constraints);

        constraints.insets.left = 10;
        colorTempSteps = new JSpinner();
        var dim = new Dimension(100, 24);
        colorTempSteps.setPreferredSize(dim);
        colorTempSteps.addChangeListener(new ColorTempAdapter());
        colorTempSteps.setModel(new ColorTempModel());
        add(colorTempSteps, constraints);

        constraints.weightx = 1;
        constraints.gridwidth = REMAINDER;
        constraints.insets.left = 20;
        colorLabel = new JLabel();
        colorLabel.setPreferredSize(dim);
        colorLabel.setMinimumSize(dim);
        colorLabel.setOpaque(true);
        add(colorLabel, constraints);
        // Mode
        var buttonGroup = new ButtonGroup();
        manualMode = new XRadioButton(new ManualModeAction());
        buttonGroup.add(manualMode);
        autoMode = new XRadioButton(new AutoModeAction());
        buttonGroup.add(autoMode);
        add(autoMode, constraints);
        add(manualMode, constraints);
        // time settings
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        add(new JLabel("Von"), constraints);
        constraints.insets.left = 10;
        fromTime = new JSpinner(new SpinnerHourModel());
        fromTime.setPreferredSize(dim);
        add(fromTime, constraints);
        constraints.insets.left = 20;
        add(new JLabel("Bis"), constraints);
        toTime = new JSpinner(new SpinnerHourModel());
        toTime.setPreferredSize(dim);
        constraints.insets.left = 10;
        constraints.weightx = 1;
        constraints.gridwidth = REMAINDER;
        add(toTime, constraints);

        // filler
        constraints.insets.left = 0;
        constraints.insets.bottom = 0;
        constraints.gridheight = REMAINDER;
        constraints.gridwidth = REMAINDER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = BOTH;
        add(Box.createGlue(), constraints);

    }

    private ColorTemp getColorTemp(JSpinner spinner) {
        var model = (ColorTempModel) spinner.getModel();
        var colorTemp = model.getColorTemp();
        return colorTemp;
    }

    private void displayColorTemp(ColorTemp colorTemp) {
        colorLabel.setBackground(new Color(colorTemp.getRed(), colorTemp.getGreen(), colorTemp.getBlue()));
        colorLabel.setToolTipText(getColorTempTooltip(colorTemp));
    }

    private void setFromToEnabled(boolean enabled) {
        fromTime.setEnabled(enabled);
        toTime.setEnabled(enabled);
    }

    private void setAllEnabled(boolean enabled) {
        this.autoMode.setEnabled(enabled);
        this.manualMode.setEnabled(enabled);
        this.fromTime.setEnabled(enabled);
        this.toTime.setEnabled(enabled);
        this.colorTempSteps.setEnabled(enabled);
    }

    void save() {
        // TODO
    }

    private class ColorTempAdapter implements ChangeListener
    {

        @Override
        public void stateChanged(ChangeEvent e) {
            var spinner = (JSpinner) e.getSource();
            var colorTemp = getColorTemp(spinner);
            displayColorTemp(colorTemp);
            IEventbus.get().post(new ColorTempEvent(colorTemp));
        }
    }

    private class ManualModeAction extends AbstractAction
    {

        private static final long serialVersionUID = 1L;

        private ManualModeAction() {
            putValue(NAME, "Manuell");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var button = (JRadioButton) e.getSource();
            setFromToEnabled(button.isSelected());
        }

    }

    private class AutoModeAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        private AutoModeAction() {
            putValue(NAME, "Automatisch");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var button = (JRadioButton) e.getSource();
            setFromToEnabled(!button.isSelected());
        }

    }

    private class NightModeAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        private NightModeAction() {
            putValue(NAME, "Nachtmodus");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var button = (JCheckBox) e.getSource();
            setAllEnabled(button.isSelected());
        }

    }

    private String getColorTempTooltip(ColorTemp ct) {
        int kelvin = ct.getKelvin();
        if (kelvin <= 1800) {
            return IGui.getI18n("colorTempPane.candle");
        }
        if (kelvin <= 2800) {
            return IGui.getI18n("colorTempPane.extraWarmWhite");
        }
        if (kelvin <= 3000) {
            return IGui.getI18n("colorTempPane.warmWhite");
        }
        if (kelvin <= 4000) {
            return IGui.getI18n("colorTempPane.coolWhite");
        }
        if (kelvin <= 5000) {
            return IGui.getI18n("colorTempPane.saylight");
        }
        if (kelvin <= 7000) {
            return IGui.getI18n("colorTempPane.overcastSky");
        }
        return IGui.getI18n("colorTempPane.blueSky");
    };

}
