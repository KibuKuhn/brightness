package kibu.kuhn.brightness.ui.xport;

import kibu.kuhn.brightness.ui.drop.DropTreeModel;
import kibu.kuhn.brightness.ui.drop.RootNode;

class NodeImportReplaceHandler implements NodeImportHandler {

  @Override
  public void accept(RootNode node) {
    DropTreeModel.instance().getRoot().removeAllChildren();
    new NodeImportMergeHandler().accept(node);
  }

}
