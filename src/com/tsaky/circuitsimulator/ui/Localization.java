package com.tsaky.circuitsimulator.ui;

import java.util.*;

public class Localization {

    private static final String bundleBaseName = "com.tsaky.circuitsimulator.messages.messages";
    private static ResourceBundle resourceBundle;
    private static final ResourceBundle defaultBundle = ResourceBundle.getBundle(bundleBaseName, new Locale("en", "US"));

    public static void setLocale(Locale locale) {
        try {
            resourceBundle = ResourceBundle.getBundle(bundleBaseName, locale);
        }catch (MissingResourceException e){
            resourceBundle = defaultBundle;
        }
    }

    public static Locale getLocale(){
        return resourceBundle.getLocale();
    }

    public static Map<String, Locale> getAvailableLocales() {
        Set<ResourceBundle> resourceBundles = new HashSet<>();
        Map<String, Locale> locales = new TreeMap<>();


        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                resourceBundles.add(ResourceBundle.getBundle(bundleBaseName, locale));
            } catch (MissingResourceException ex) {

            }
        }
        for(ResourceBundle resourceBundle : resourceBundles){
            Locale locale = resourceBundle.getLocale();

            String displayLanguage = locale.getDisplayLanguage(locale);
            String displayCountry = locale.getDisplayCountry(locale);

            String string = displayLanguage;

            if(!displayCountry.equals(""))string += " (" + displayCountry + ")";

            locales.put(string, locale);
        }

        return locales;
    }
    public static String getString(String key){
        String string;

        if(resourceBundle.containsKey(key)){
            string = resourceBundle.getString(key);
        }else if(defaultBundle.containsKey(key)){
            string = defaultBundle.getString(key);
        }else{
            System.err.println("Couldn't find locale key " + key);
            string = "#MISSING_STRING";
        }

        return string;
    }


}
