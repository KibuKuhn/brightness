package kibu.kuhn.brightness.ui;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.REMAINDER;
import static java.awt.GridBagConstraints.WEST;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import kibu.kuhn.brightness.colortemp.IColorTempService;
import kibu.kuhn.brightness.domain.ColorTemp;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.ui.component.XButton;
import kibu.kuhn.brightness.ui.component.XCheckBox;
import kibu.kuhn.brightness.ui.component.XRadioButton;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class ColorTempPane extends JPanel
{

    private static final long serialVersionUID = 1L;

    private JLabel colorLabel;
    private JSpinner colorTempSteps;
    private JSpinner fromTime;
    private JSpinner toTime;

    private XRadioButton manualMode;

    private XRadioButton autoMode;

    private XCheckBox colorTempCheckbox;

    private JButton defaultColorTempButton;

    @Inject
    private IPreferencesService preferences;
    @Inject
    private I18n i18n;
    @Inject
    private IColorTempService colorTempService;
    @Inject
    private Icons icons;

    public ColorTempPane() {
        initUI();
        init();
    }

    private void init() {
        colorTempSteps.setModel(new ColorTempModel(colorTempService.getColorTempValues()));
        boolean mode = preferences.isColorTemp();
        colorTempCheckbox.setSelected(mode);
        applyColorTemp(mode);
        colorTempService.setTestMode(mode);

        int kelvin = preferences.getColorTempKelvin();
        colorTempSteps.getModel().setValue(kelvin);

        boolean autoMode = preferences.isColorTempAutoMode();
        if (autoMode) {
            this.autoMode.setSelected(true);
        } else {
            manualMode.setSelected(true);
        }
        ((SpinnerHourModel) fromTime.getModel()).setValue(preferences.getColorTempFromTime());
        ((SpinnerHourModel) toTime.getModel()).setValue(preferences.getColorTempToTime());
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
        colorTempCheckbox = new XCheckBox(new ColorTempAction());
        add(colorTempCheckbox, constraints);

        constraints.weightx = 0;
        constraints.gridwidth = 1;
        add(new JLabel("Kelvin"), constraints);

        constraints.insets.left = 10;
        colorTempSteps = new JSpinner();
        var dim = new Dimension(100, 24);
        colorTempSteps.setPreferredSize(dim);
        colorTempSteps.addChangeListener(new ColorTempChangeAdapter());
        add(colorTempSteps, constraints);

        constraints.insets.left = 20;
        colorLabel = new JLabel();
        colorLabel.setPreferredSize(dim);
        colorLabel.setMinimumSize(dim);
        colorLabel.setOpaque(true);
        add(colorLabel, constraints);

        constraints.weightx = 1;
        constraints.gridwidth = REMAINDER;
        defaultColorTempButton = new XButton(new DefaultColorTempAction());
        add(defaultColorTempButton, constraints);
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
        add(new JLabel(i18n.get("colorTempPane.from")), constraints);
        constraints.insets.left = 10;
        fromTime = new JSpinner(new SpinnerHourModel());
        fromTime.setPreferredSize(dim);
        add(fromTime, constraints);
        constraints.insets.left = 20;
        add(new JLabel(i18n.get("colorTempPane.to")), constraints);
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
        this.defaultColorTempButton.setEnabled(enabled);
    }

    void save() {
        preferences.setColorTemp(colorTempCheckbox.isSelected());
        preferences.setColorTempAutoMode(autoMode.isSelected());
        preferences.setColorTempKelvin(getColorTemp(colorTempSteps).getKelvin());
        preferences.setColorTempFromTime(((SpinnerHourModel) fromTime.getModel()).getValue().getTime());
        preferences.setColorTempToTime(((SpinnerHourModel) toTime.getModel()).getValue().getTime());
        colorTempService.setTestMode(false);
    }

    private class ColorTempChangeAdapter implements ChangeListener
    {

        @Override
        public void stateChanged(ChangeEvent e) {
            var spinner = (JSpinner) e.getSource();
            var colorTemp = getColorTemp(spinner);
            displayColorTemp(colorTemp);
            colorTempService.updateColorTemp(colorTemp);
        }
    }

    private class ManualModeAction extends AbstractAction
    {

        private static final long serialVersionUID = 1L;

        private ManualModeAction() {
            putValue(NAME, i18n.get("colorTempPane.mode.manual"));
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
            putValue(NAME, i18n.get("colorTempPane.mode.auto"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var button = (JRadioButton) e.getSource();
            setFromToEnabled(!button.isSelected());
        }

    }

    private class ColorTempAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;

        private ColorTempAction() {
            putValue(NAME, i18n.get("colorTempPane.nightshift"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var button = (JCheckBox) e.getSource();
            applyColorTemp(button.isSelected());
            if (button.isSelected()) {
                colorTempService.setTestMode(true);
            }
        }

    }

    private String getColorTempTooltip(ColorTemp ct) {
        int kelvin = ct.getKelvin();
        if (kelvin <= 1800) {
            return i18n.get("colorTempPane.candle");
        }
        if (kelvin <= 2800) {
            return i18n.get("colorTempPane.extraWarmWhite");
        }
        if (kelvin <= 3000) {
            return i18n.get("colorTempPane.warmWhite");
        }
        if (kelvin <= 4000) {
            return i18n.get("colorTempPane.coolWhite");
        }
        if (kelvin <= 5000) {
            return i18n.get("colorTempPane.saylight");
        }
        if (kelvin <= 7000) {
            return i18n.get("colorTempPane.overcastSky");
        }
        return i18n.get("colorTempPane.blueSky");
    }

    private void applyColorTemp(boolean enabled) {
        setAllEnabled(enabled);
        colorTempService.setColorTempEnabled(enabled);
    };

    private class DefaultColorTempAction extends AbstractAction
    {

        private static final long serialVersionUID = 1L;

        private DefaultColorTempAction() {
            putValue(LARGE_ICON_KEY, icons.getIcon("circleDown24"));
            putValue(SHORT_DESCRIPTION, i18n.get("colorTempPane.defaultKelvin"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ColorTemp colorTemp = getColorTemp(colorTempSteps);
            preferences.setDefaultColorTempKelvin(colorTemp.getKelvin());
        }

    }

}
