package kibu.kuhn.brightness.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import kibu.kuhn.brightness.prefs.IPreferencesService;

public class I18nService implements I18n
{

    private static final String BUNDLE_NAME = "i18n";

    @Inject
    private IPreferencesService preferences;
    private ResourceBundle bundle;

    @Override
    public String get(String key) {
        return bundle.getString(key);
    }

    @PostConstruct
    public void postConstruct() {
        Locale.setDefault(Locale.ENGLISH);
        Locale language = preferences.getLocale();
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, language);
    }

}
