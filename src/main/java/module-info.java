module brightness {
  exports kibu.kuhn.brightness;
  exports kibu.kuhn.brightness.prefs;
  requires java.desktop;
  requires org.slf4j;
  requires java.scripting;
  requires java.prefs;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.databind;
  opens kibu.kuhn.brightness.domain to com.fasterxml.jackson.databind;
}
