package kibu.kuhn.brightness.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import kibu.kuhn.brightness.utils.Injection;

@Injection
public class ExitAction extends AbstractMenuAction
{

    private static final long serialVersionUID = 1L;

    public ExitAction(Consumer<? super ActionEvent> action) {
        super(action);
        putValue(NAME, i18n.get("settingsmenu.button.exit"));
        putValue(SMALL_ICON, icons.getIcon("cancel18"));
    }
}