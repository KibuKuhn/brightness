package kibu.kuhn.brightness.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.swing.AbstractAction;

public abstract class AbstractMenuAction extends AbstractAction
{

    private static final long serialVersionUID = 1L;

    @Inject
    protected Icons icons;
    @Inject
    protected I18n i18n;

    protected Consumer<? super ActionEvent> action;

    public AbstractMenuAction(Consumer<? super ActionEvent> action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        action.accept(e);
    }
}