package kibu.kuhn.brightness.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class ExitAction extends AbstractMenuAction
{

    private static final long serialVersionUID = 1L;

    public ExitAction(Consumer<? super ActionEvent> action) {
        super(action);
        putValue(NAME, IGui.get().getI18n("settingsmenu.button.exit"));
        putValue(SMALL_ICON, Icons.getIcon("cancel18"));
    }
}