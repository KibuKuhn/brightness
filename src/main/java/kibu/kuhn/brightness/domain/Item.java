package kibu.kuhn.brightness.domain;

import javax.swing.Icon;


public interface Item {

  Type getType();

  String getAlias();

  void setAlias(String alias);

  Icon getIcon();

  String getDisplayString();
}
