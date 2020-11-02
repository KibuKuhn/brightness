package kibu.kuhn.brightness.prefs;

import java.awt.Point;
import java.io.File;
import java.util.Locale;
import javax.swing.UIManager.LookAndFeelInfo;

import kibu.kuhn.brightness.domain.DisplayUnit;
import kibu.kuhn.brightness.ui.drop.RootNode;


public interface IPreferencesService {

  @SuppressWarnings("exports")
  void saveItems(RootNode node);

  @SuppressWarnings("exports")
  RootNode getItems();

  @SuppressWarnings("exports")
  void saveLaf(LookAndFeelInfo laf);

  void saveLocale(Locale locale);

  @SuppressWarnings("exports")
  LookAndFeelInfo getLaf();

  Locale getLocale();

  static IPreferencesService get() {
    return PreferencesService.get();
  }

  File getExportPath();

  void saveExportPath(String absolutePath);

  void saveConfirmDeleteItem(boolean selected);

  boolean isConfirmDeleteItem();

  boolean isMainMenuLocationUpdatEnabled();

  void setMainMenuLocationUpdateNabled(boolean enabled);

  @SuppressWarnings("exports")
  void saveMainMenuLocation(Point locationOnScreen);

  @SuppressWarnings("exports")
  Point getMainMenuLocation();

  void setDarkMode(boolean selected);

  boolean isDarkMode();
  
  DisplayUnit getBrightness(DisplayUnit unit);

}

