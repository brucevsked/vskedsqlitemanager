package com.vsked.sqlitemanager.services;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;


/**
 * this class copy from <a href="https://riptutorial.com/javafx/example/23068/switching-language-dynamically-when-the-application-is-running">Switching language dynamically when the application is running</a>
 *
 */
public class I18N {
    /** the current selected Locale. */
    private static final ObjectProperty<Locale> locale;

    static {
        locale = new SimpleObjectProperty<>(getDefaultLocale());
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    /**
     * get the supported Locales.
     *
     * @return List of Locale objects.
     */
    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.CHINESE));
    }

    /**
     * get the default locale. This is the systems default if contained in the supported locales, english otherwise.
     *
     * @return Locale
     */
    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
    }

    public static Locale getLocale() {
        return locale.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    /**
     * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning the result.
     *
     * @param key
     *         message key
     * @param args
     *         optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }

    /**
     * creates a String binding to a localized String for the given message bundle key
     *
     * @param key
     *         key
     * @return String binding
     */
    public static StringBinding createStringBinding(final String key, Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }

    /**
     * creates a String Binding to a localized String that is computed by calling the given func
     *
     * @param func
     *         function called on every change
     * @return StringBinding
     */
    public static StringBinding createStringBinding(Callable<String> func) {
        return Bindings.createStringBinding(func, locale);
    }

    /**
     * creates a bound Label whose value is computed on language change.
     *
     * @param func
     *         the function to compute the value
     * @return Label
     */
    public static Label labelForValue(Callable<String> func) {
        Label label = new Label();
        label.textProperty().bind(createStringBinding(func));
        return label;
    }

    /**
     * creates a bound Button for the given resourcebundle key
     *
     * @param key
     *         ResourceBundle key
     * @param args
     *         optional arguments for the message
     * @return Button
     */
    public static Button buttonForKey(final String key, final Object... args) {
        Button button = new Button();
        button.textProperty().bind(createStringBinding(key, args));
        return button;
    }

    public static Menu menuForKey(final String key, final Object... args) {
        Menu menu=new Menu();
        menu.textProperty().bind(createStringBinding(key, args));
        return menu;
    }

    public static MenuItem menuItemForKey(final String key, final Object... args) {
        MenuItem menuItem=new MenuItem();
        menuItem.textProperty().bind(createStringBinding(key, args));
        return menuItem;
    }

    public static TreeItem<String> treeItemForKey(final String key, final Object... args) {
        TreeItem<String> treeItem=new TreeItem<String>();
        treeItem.valueProperty().bind(createStringBinding(key, args));
        return treeItem;
    }

    public static Alert alertForKey(Alert.AlertType alertType, final String titleKey, final String headerTextKey, final String contentTextKey, final Object... args){
        Alert alert = new Alert(alertType);
        alert.titleProperty().bind(createStringBinding(titleKey,args));
        alert.headerTextProperty().bind(createStringBinding(headerTextKey,args));
        alert.contentTextProperty().bind(createStringBinding(contentTextKey,args));
        return alert;
    }

    public static Alert alertOnlyContentForKey(Alert.AlertType alertType, final String contentTextKey, final Object... args){
        Alert alert = new Alert(alertType);
        alert.contentTextProperty().bind(createStringBinding(contentTextKey,args));
        return alert;
    }


}
