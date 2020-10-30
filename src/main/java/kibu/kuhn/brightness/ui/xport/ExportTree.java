package kibu.kuhn.brightness.ui.xport;

import static javax.swing.tree.TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION;

import kibu.kuhn.brightness.ui.drop.DropTree;

class ExportTree extends DropTree {

  private static final long serialVersionUID = 1L;

  ExportTree() {
    getSelectionModel().setSelectionMode(DISCONTIGUOUS_TREE_SELECTION);
    getSelectionModel().addTreeSelectionListener(null);
  }

}
