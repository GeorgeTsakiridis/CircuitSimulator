package com.tsaky.circuitsimulator.ui;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {

    private static final String bundleBaseName = "com.tsaky.circuitsimulator.messages.messages";
    private static ResourceBundle resourceBundle;
    private static final ResourceBundle defaultBundle = ResourceBundle.getBundle(bundleBaseName, new Locale("en", "US"));

    public static void setLocale(Locale locale){
        resourceBundle = ResourceBundle.getBundle(bundleBaseName, locale);
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
