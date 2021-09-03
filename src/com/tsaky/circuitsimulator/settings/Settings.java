package com.tsaky.circuitsimulator.settings;

import com.tsaky.circuitsimulator.ChipSimulator;
import com.tsaky.circuitsimulator.ui.Localization;

import javax.swing.*;
import java.io.*;
import java.util.Locale;

public class Settings {

    public static void saveToFile(File file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

        dataOutputStream.writeLong(ChipSimulator.MAGIC_NUMBER_CONF_SAVE);
        Locale locale = Localization.getLocale();
        dataOutputStream.writeUTF(locale.getLanguage());
        dataOutputStream.writeUTF(locale.getCountry());

    }

    public static void loadFromFile(File file) throws IOException{
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));

        Long magicNumber = dataInputStream.readLong();
        if(magicNumber.equals(ChipSimulator.MAGIC_NUMBER_CONF_SAVE)){
            JOptionPane.showMessageDialog(null, Localization.getString("invalid_configuration_file"),
                    Localization.getString("failed_to_open_configuration_file"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        Localization.setLocale(new Locale(dataInputStream.readUTF(), dataInputStream.readUTF()));

    }

}
