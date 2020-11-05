package kibu.kuhn.brightness.event;

import java.awt.event.MouseEvent;

public class MainMenuPositionEvent extends Event<MouseEvent>
{

    public MainMenuPositionEvent(MouseEvent delegate) {
        super(delegate);
    }

}
