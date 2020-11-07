package kibu.kuhn.brightness.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import kibu.kuhn.brightness.utils.Injection;

@Injection
public class HelpAction extends AbstractMenuAction
{

    private static final long serialVersionUID = 1L;

    public HelpAction(Consumer<? super ActionEvent> action) {
        super(action);
        putValue(NAME, i18n.get("settingsmenu.button.help"));
        putValue(SMALL_ICON, icons.getIcon("help18"));
    }
}