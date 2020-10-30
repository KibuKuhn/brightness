package kibu.kuhn.brightness.ui.drop.filter;

import java.io.File;
import java.io.IOException;

import kibu.kuhn.brightness.util.ISystemUtils;

class WindowsFileFilter extends FileFilter {

  @Override
  protected boolean isExecutable(File file) {
    return false;
  }

  @Override
  protected boolean analyze(File file) throws IOException {
    if (isExecutable(file)) {
      return false;
    }

    if (ISystemUtils.get().isTempFile(file)) {
      return false;
    }

    return true;
  }
}
