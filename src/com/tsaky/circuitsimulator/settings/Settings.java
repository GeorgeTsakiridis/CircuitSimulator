package com.tsaky.circuitsimulator.settings;

import com.tsaky.circuitsimulator.ChipSimulator;
import com.tsaky.circuitsimulator.ui.Localization;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Locale;

public class Settings {

    public static void save() throws IOException {
        File file = getConfigFile();

        if(file == null){
            return;
        }

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

        dataOutputStream.writeLong(ChipSimulator.MAGIC_NUMBER_CONF_SAVE);
        Locale locale = Localization.getLocale();
        dataOutputStream.writeUTF(locale.getLanguage());
        dataOutputStream.writeUTF(locale.getCountry());

    }

    public static void load() throws IOException {
        File configFile = getConfigFile();
        if (configFile == null) return;

        if (configFile.exists()) {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(configFile));

            Long magicNumber = dataInputStream.readLong();
            if (!magicNumber.equals(ChipSimulator.MAGIC_NUMBER_CONF_SAVE)) {
                JOptionPane.showMessageDialog(null, Localization.getString("invalid_configuration_file"),
                        Localization.getString("failed_to_open_configuration_file"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Locale locale = new Locale(dataInputStream.readUTF(), dataInputStream.readUTF());

            Localization.setLocale(locale);

        }
    }
    private static File getConfigFile(){
        try {
            return new File(new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent() + "/configuration.cscf");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

}
