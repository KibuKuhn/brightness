package kibu.kuhn.brightness.domain;

public interface CloneableItem extends Item {

  CloneableItem clone() throws CloneNotSupportedException;
}
