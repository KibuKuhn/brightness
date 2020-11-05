package kibu.kuhn.brightness.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class HelpAction extends AbstractMenuAction
{

    private static final long serialVersionUID = 1L;

    public HelpAction(Consumer<? super ActionEvent> action) {
        super(action);
        putValue(NAME, IGui.get().getI18n("settingsmenu.button.help"));
        putValue(SMALL_ICON, Icons.getIcon("help18"));
    }
}