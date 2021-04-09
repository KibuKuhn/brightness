package kibu.kuhn.brightness.utils;

import static com.google.inject.Scopes.SINGLETON;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import kibu.kuhn.brightness.colortemp.ColorTempService;
import kibu.kuhn.brightness.colortemp.IColorTempService;
import kibu.kuhn.brightness.displayunit.DisplayUnitManager;
import kibu.kuhn.brightness.displayunit.IDisplayUnitManager;
import kibu.kuhn.brightness.event.Eventbus;
import kibu.kuhn.brightness.event.IEventbus;
import kibu.kuhn.brightness.prefs.IPreferencesService;
import kibu.kuhn.brightness.prefs.PreferencesService;
import kibu.kuhn.brightness.ui.Gui;
import kibu.kuhn.brightness.ui.I18n;
import kibu.kuhn.brightness.ui.I18nService;
import kibu.kuhn.brightness.ui.IGui;
import kibu.kuhn.brightness.ui.Icons;
import kibu.kuhn.brightness.ui.IconsFactory;

public class GuiceModule extends AbstractModule implements TypeListener
{

    private final class PostConstructAdapter<I> implements InjectionListener<I>
    {
        @Override
        public void afterInjection(final I injectee) {
            //@formatter:off
            Arrays.asList(injectee.getClass().getMethods())
                  .stream()
                  .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                  .forEach(method -> invoke(method, injectee));
            //@formatter:on
        }

        private void invoke(Method method, Object injectee) {
            try {
                method.invoke(injectee);
            } catch (Exception ex) {
                throw new IllegalStateException(String.format("@PostConstruct %s", method), ex);
            }
        }
    }

    @Override
    protected void configure() {
        binder().bindListener(Matchers.any(), this);
        bind(Icons.class).to(IconsFactory.class).in(SINGLETON);
        bind(IPreferencesService.class).to(PreferencesService.class).in(SINGLETON);
        bind(I18n.class).to(I18nService.class).in(SINGLETON);
        bind(IEventbus.class).to(Eventbus.class).in(SINGLETON);
        bind(IDisplayUnitManager.class).to(DisplayUnitManager.class).asEagerSingleton();
        bind(IGui.class).to(Gui.class).in(SINGLETON);
        bind(IColorTempService.class).to(ColorTempService.class).in(SINGLETON);
    }

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        encounter.register(new PostConstructAdapter<I>());
    }

}
