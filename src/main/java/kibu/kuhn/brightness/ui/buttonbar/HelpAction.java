package kibu.kuhn.brightness.ui.buttonbar;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import kibu.kuhn.brightness.ui.IGui;
import kibu.kuhn.brightness.ui.Icons;

class HelpAction extends AbstractMenuAction
{

    private static final long serialVersionUID = 1L;

    HelpAction(Consumer<? super ActionEvent> action) {
        super(action);
        putValue(SHORT_DESCRIPTION, IGui.get().getI18n("mainmenu.buttonbar.button.help"));
        putValue(SMALL_ICON, Icons.getIcon("help18"));
        // TODO
        // putValue(ACTION_COMMAND_KEY, ACTION_HELP);
    }
}