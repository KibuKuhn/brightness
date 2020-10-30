package kibu.kuhn.brightness.ui.xport;

import java.util.function.Consumer;

import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.ui.drop.RootNode;

public interface NodeImportHandler extends Consumer<RootNode> {

  default void save(RootNode node) {
    IPreferencesService.get().saveItems(node);
  }

}
