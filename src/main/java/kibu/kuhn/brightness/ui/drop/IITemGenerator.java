package kibu.kuhn.brightness.ui.drop;

import java.util.List;
import java.util.function.Function;

import kibu.kuhn.brightness.domain.Item;
import kibu.kuhn.brightness.util.ISystemUtils;

interface IITemGenerator extends Function<TransferData, List<Item>> {

  static IITemGenerator get() {
    return ISystemUtils.get().isWindows() ? new WindowsItemGenerator() : new ItemGenerator();
  }

}