package com.vsked.sqlitemanager.ui;

import com.vsked.sqlitemanager.services.I18N;

import java.util.Locale;

public class LanguageManager {
    /**
     * sets the given Locale in the I18N class and keeps count of the number of switches.
     *
     * @param locale the new local to set
     */
    public static void switchLanguage(Locale locale) {
        I18N.setLocale(locale);
    }
}
