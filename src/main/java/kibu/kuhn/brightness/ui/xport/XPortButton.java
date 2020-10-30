package kibu.kuhn.brightness.ui.xport;

import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

import kibu.kuhn.brightness.ui.IGui;
import kibu.kuhn.brightness.ui.Icons;

public class XPortButton extends JCheckBox {

  private static final long serialVersionUID = 1L;

  public XPortButton() {
    setContentAreaFilled(false);
    setIcon(Icons.getIcon("right18"));
    setText(IGui.get().getI18n("xportbutton.label"));
  }

  @Override
  protected void fireActionPerformed(ActionEvent event) {
    setIcon(isSelected() ? Icons.getIcon("down18") : Icons.getIcon("right18"));
    super.fireActionPerformed(event);
  }
}
