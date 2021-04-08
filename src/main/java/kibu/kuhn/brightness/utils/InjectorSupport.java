package kibu.kuhn.brightness.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class InjectorSupport
{
    private static InjectorSupport instance = new InjectorSupport();

    public static InjectorSupport get() {
        return instance;
    }

    private volatile Injector injector;

    private InjectorSupport() {
        injector = Guice.createInjector(new GuiceModule());
    }

    public void injectMembers(Object instance) {
        injector.injectMembers(instance);
    }

}
