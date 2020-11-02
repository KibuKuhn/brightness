package kibu.kuhn.brightness.ui;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import kibu.kuhn.brightness.displayunit.IDisplayUnitManager;
import kibu.kuhn.brightness.domain.DisplayUnit;
import kibu.kuhn.brightness.prefs.IPreferencesService;

public class SliderPane extends JPanel
{

    private static final long serialVersionUID = 1L;

    public SliderPane() {
        init();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        var units = IDisplayUnitManager.get().getDisplayUnits();
        for (int i = 0; i < units.size(); i++) {
            initSliders(units.get(i), i);
        }
    }

    private void initSliders(DisplayUnit unit, int index) {
        unit = IPreferencesService.get().getBrightness(unit);
        var model = new DefaultBoundedRangeModel();
        model.setValue(unit.getValue());
        JPanel pane = null;
        if (index > 0) {
            pane = new JPanel() {

                private static final long serialVersionUID = 1L;

                @Override
                public Insets getInsets() {
                    var insets = super.getInsets();
                    insets.left = 10;
                    return insets;
                }
            };
        } else {
            pane = new JPanel();
            pane.setLayout(new BorderLayout());
        }
        pane.setLayout(new BorderLayout());
        pane.add(new JLabel(unit.getName()), BorderLayout.NORTH);
        var slider = new JSlider(JSlider.VERTICAL);
        slider.setModel(model);
        pane.add(slider);
        add(pane);
    }

}
