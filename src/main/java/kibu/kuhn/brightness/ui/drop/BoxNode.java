package kibu.kuhn.brightness.ui.drop;

import kibu.kuhn.brightness.domain.BoxItem;
import kibu.kuhn.brightness.domain.Item;

public class BoxNode extends DropTreeNode {

  private static final long serialVersionUID = 1L;

  public BoxNode() {
    this(new BoxItem());
  }

  public BoxNode(BoxItem item) {
    super.setUserObject(item);
  }

  @Override
  public void setUserObject(Object item) {
    throw new UnsupportedOperationException("setItem not supported");
  }

  @Override
  public void setItem(Item item) {
    throw new UnsupportedOperationException("setItem not supported");
  }

  @Override
  public boolean isLeaf() {
    return false;
  }


}
