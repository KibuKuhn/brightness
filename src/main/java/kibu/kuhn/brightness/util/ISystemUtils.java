package kibu.kuhn.brightness.util;

import java.io.File;

public interface ISystemUtils {

  boolean isWindows();

  boolean isTempFile(File f);

  static ISystemUtils get() {
    return SystemUtils.get();
  }

}