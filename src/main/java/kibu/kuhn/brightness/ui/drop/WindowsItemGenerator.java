package kibu.kuhn.brightness.ui.drop;

import java.io.File;

class WindowsItemGenerator extends ItemGenerator {

  @Override
  protected boolean isDesktopFile(File f) {
    return f.getName().endsWith(".URL");
  }
}
