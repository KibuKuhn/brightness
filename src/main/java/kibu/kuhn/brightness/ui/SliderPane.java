package kibu.kuhn.brightness.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import com.google.common.eventbus.Subscribe;

import kibu.kuhn.brightness.displayunit.IDisplayUnitManager;
import kibu.kuhn.brightness.domain.DisplayUnit;
import kibu.kuhn.brightness.event.AllUnitsEvent;
import kibu.kuhn.brightness.event.EventBusSupport;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.event.MainMenuPositionEvent;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.utils.Injection;

@Injection
public class SliderPane extends JPanel implements EventBusSupport
{

    private static final long serialVersionUID = 1L;

    private boolean isAllUnits;
    private List<DisplayUnit> units;
    private Set<JSlider> sliders;
    private JSlider firstSlider;
    @Inject
    private IEventbus eventbus;
    @Inject
    private IPreferencesService preferences;
    @Inject
    private IDisplayUnitManager displayUnitManager;

    public SliderPane() {
        init();
    }

    private void init() {
        sliders = new HashSet<>();
        eventbus.register(this);
        isAllUnits = preferences.isAllUnits();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        units = displayUnitManager.getDisplayUnits();
        for (int i = 0; i < units.size(); i++) {
            initSliders(units.get(i), i);
        }

        allUnitsChanged(new AllUnitsEvent(isAllUnits));
    }

    private void initSliders(DisplayUnit displayUnit, int index) {
        var unit = preferences.getBrightness(displayUnit);
        var model = new DefaultBoundedRangeModel();
        model.setMinimum(20);
        model.setValue(unit.getValue());
        var slider = new JSlider(JSlider.VERTICAL);
        slider.setModel(model);
        sliders.add(slider);
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(slider);
        JLabel label = null;
        if (index == 0) {
            firstSlider = slider;
            label = new JLabel(unit.getName());
            label.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    eventbus.post(new MainMenuPositionEvent(e));
                }
            });
        } else {
            label = new JLabel("  " + unit.getName());
            pane.add(new LinkLabel(), BorderLayout.WEST);
        }
        pane.add(label, BorderLayout.NORTH);

        if (isAllUnits && index == 0) {
            model.addChangeListener(event -> handleChangeEvent(event, unit));
        } else if (!isAllUnits) {
            model.addChangeListener(event -> handleChangeEvent(event, unit));
        }
        add(pane);
    }

    private void handleChangeEvent(ChangeEvent event, DisplayUnit unit) {
        var source = (BoundedRangeModel) event.getSource();
        unit.setValue(source.getValue());
        final DisplayUnit du;
        if (isAllUnits) {
            du = unit.clone();
            du.toAllUnits();
            updateLinkedSliders(unit);
        } else {
            du = unit;
        }
        if (!source.getValueIsAdjusting()) {
            if (isAllUnits) {
                units.forEach(u -> {
                    var unit2 = new DisplayUnit(u.getName(), du.getValue());
                    preferences.setBrightness(unit2);
                });
            } else {
                preferences.setBrightness(du);
            }
        }
        displayUnitManager.updateBrightness(du);
    }

    private void updateLinkedSliders(DisplayUnit refUnit) {
        //@formatter:off
        sliders.stream()
               .filter(s -> s != firstSlider)
               .forEach(s -> s.getModel().setValue(refUnit.getValue()));
        //@formatter:on
    }

    @Override
    public void unregister() {
        eventbus.unregister(this);
    }

    @Subscribe
    void allUnitsChanged(AllUnitsEvent event) {
        this.isAllUnits = event.getDelegate();
        // @formatter:off
        sliders.stream()
               .filter(s -> s != firstSlider)
               .forEach(s -> s.setEnabled(!isAllUnits));
        // @formatter:on

    }
}
