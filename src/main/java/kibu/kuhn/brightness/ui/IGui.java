package kibu.kuhn.brightness.ui;

public interface IGui
{
    void init();

    boolean checkSupport();

    static IGui get() {
        return Gui.get();
    }

    static String getI18n(String key) {
        return Gui.get().getI18n(key);
    }

}
