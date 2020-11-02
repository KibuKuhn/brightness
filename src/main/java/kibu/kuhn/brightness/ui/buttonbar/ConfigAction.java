package kibu.kuhn.brightness.ui.buttonbar;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import kibu.kuhn.brightness.ui.IGui;
import kibu.kuhn.brightness.ui.Icons;

class ConfigAction extends AbstractMenuAction
{

    private static final long serialVersionUID = 1L;

    ConfigAction(Consumer<? super ActionEvent> action) {
        super(action);
        putValue(SHORT_DESCRIPTION, IGui.get().getI18n("mainmenu.buttonbar.button.config"));
        putValue(SMALL_ICON, Icons.getIcon("favorites18"));
//TODO
        // putValue(ACTION_COMMAND_KEY, ButtonBar.ACTION_CONFIG);
    }
}