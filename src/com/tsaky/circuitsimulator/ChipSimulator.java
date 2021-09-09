package com.tsaky.circuitsimulator;

import com.tsaky.circuitsimulator.chip.ChipManager;
import com.tsaky.circuitsimulator.chip.ChipNode;
import com.tsaky.circuitsimulator.chip.ChipText;
import com.tsaky.circuitsimulator.chip.c74series.*;
import com.tsaky.circuitsimulator.chip.generic.*;
import com.tsaky.circuitsimulator.chip.other.Chip16bitInputLCD;
import com.tsaky.circuitsimulator.chip.other.Chip16bitOutputLCD;
import com.tsaky.circuitsimulator.chip.other.ChipPD43256BCZ;
import com.tsaky.circuitsimulator.logic.Handler;
import com.tsaky.circuitsimulator.settings.Settings;
import com.tsaky.circuitsimulator.ui.Localization;
import com.tsaky.circuitsimulator.ui.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class ChipSimulator {

    public static final int PROGRAM_VERSION = 1090521;
    public static final String PROGRAM_VERSION_STRING = "1.0.0";
    public static final long MAGIC_NUMBER_PROJ_SAVE = 1073147341387874804L;
    public static final long MAGIC_NUMBER_CONF_SAVE = 1123140789477211792L;

    public ChipSimulator() {
        Localization.setLocale(Locale.getDefault());

        try{
            Settings.load();
        }catch (Exception e) {
            System.err.println("Failed to load settings");
            e.printStackTrace();
            System.exit(1);
        }

        ResourceManager.addResource("mf_add", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "add.png");
        ResourceManager.addResource("mf_link", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "link.png");
        ResourceManager.addResource("mf_snap_locked", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "mouse_snap_locked.png");
        ResourceManager.addResource("mf_snap_unlocked", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "mouse_snap_unlocked.png");
        ResourceManager.addResource("mf_move", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "move.png");
        ResourceManager.addResource("mf_remove", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "remove.png");
        ResourceManager.addResource("mf_text", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "text.png");
        ResourceManager.addResource("mf_toggle", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "toggle.png");
        ResourceManager.addResource("mf_viewport_move", ResourceManager.EnumSubFolder.MOUSE_FUNCTION, "viewport_move.png");

        ResourceManager.addResource("proj_load", ResourceManager.EnumSubFolder.PROJECT, "load.png");
        ResourceManager.addResource("proj_new", ResourceManager.EnumSubFolder.PROJECT, "new.png");
        ResourceManager.addResource("proj_save", ResourceManager.EnumSubFolder.PROJECT, "save.png");

        ResourceManager.addResource("sim_start", ResourceManager.EnumSubFolder.SIMULATION, "start.png");
        ResourceManager.addResource("sim_step", ResourceManager.EnumSubFolder.SIMULATION, "step.png");
        ResourceManager.addResource("sim_stop", ResourceManager.EnumSubFolder.SIMULATION, "stop.png");
        ResourceManager.addResource("sim_increase_speed", ResourceManager.EnumSubFolder.SIMULATION, "increase_speed.png");
        ResourceManager.addResource("sim_decrease_speed", ResourceManager.EnumSubFolder.SIMULATION, "decrease_speed.png");

        ResourceManager.addResource("view_chip_custom_name", ResourceManager.EnumSubFolder.VIEW, "chip_custom_name.png");
        ResourceManager.addResource("view_chip_real_name", ResourceManager.EnumSubFolder.VIEW, "chip_real_name.png");
        ResourceManager.addResource("view_grid_toggle", ResourceManager.EnumSubFolder.VIEW, "grid_toggle.png");
        ResourceManager.addResource("view_line_normal", ResourceManager.EnumSubFolder.VIEW, "line_normal_view.png");
        ResourceManager.addResource("view_line_power", ResourceManager.EnumSubFolder.VIEW, "line_power_view.png");
        ResourceManager.addResource("view_line_status", ResourceManager.EnumSubFolder.VIEW, "line_status_view.png");
        ResourceManager.addResource("view_pin_normal", ResourceManager.EnumSubFolder.VIEW, "pin_normal_view.png");
        ResourceManager.addResource("view_pin_status", ResourceManager.EnumSubFolder.VIEW, "pin_status_view.png");
        ResourceManager.addResource("view_pin_type", ResourceManager.EnumSubFolder.VIEW, "pin_type_view.png");
        ResourceManager.addResource("view_show_component_info", ResourceManager.EnumSubFolder.VIEW, "show_component_info.png");
        ResourceManager.addResource("view_zoom_in", ResourceManager.EnumSubFolder.VIEW, "zoom_in.png");
        ResourceManager.addResource("view_zoom_out", ResourceManager.EnumSubFolder.VIEW, "zoom_out.png");
        ResourceManager.addResource("view_zoom_reset", ResourceManager.EnumSubFolder.VIEW, "zoom_reset.png");

        ResourceManager.addResource("cmp_horizontal_segment", ResourceManager.EnumSubFolder.COMPONENT, "horizontal_segment.png");
        ResourceManager.addResource("cmp_vertical_segment", ResourceManager.EnumSubFolder.COMPONENT, "vertical_segment.png");

        ResourceManager.addResource("help", ResourceManager.EnumSubFolder.OTHER, "help.png");
        ResourceManager.addResource("about", ResourceManager.EnumSubFolder.OTHER, "about.png");
        ResourceManager.addResource("settings", ResourceManager.EnumSubFolder.OTHER, "settings.png");
        ResourceManager.addResource("build", ResourceManager.EnumSubFolder.OTHER, "build.png");

        ChipManager.addChip(new ChipNode());
        ChipManager.addChip(new ChipGround());
        ChipManager.addChip(new ChipPower());
        ChipManager.addChip(new ChipPowerSwitch());
        ChipManager.addChip(new ChipSwitch());
        ChipManager.addChip(new ChipRelay());
        ChipManager.addChip(new ChipDiode());
        ChipManager.addChip(new ChipText());
        ChipManager.addChip(new ChipLED("Red", Localization.getString("red"), Color.RED));
        ChipManager.addChip(new ChipLED("Green", Localization.getString("green"), Color.GREEN));
        ChipManager.addChip(new ChipLED("Yellow", Localization.getString("yellow"), Color.YELLOW));
        ChipManager.addChip(new ChipLED("Blue", Localization.getString("blue"), Color.BLUE));
        ChipManager.addChip(new Chip7SegmentDisplay());

        ChipManager.addChip(new Chip7402());
        ChipManager.addChip(new Chip7404());
        ChipManager.addChip(new Chip7408());
        ChipManager.addChip(new Chip7432());
        ChipManager.addChip(new Chip7486());
        ChipManager.addChip(new Chip74257());
        ChipManager.addChip(new Chip74283());
        ChipManager.addChip(new Chip74573());
        ChipManager.addChip(new Chip74574());
        ChipManager.addChip(new Chip744511());

        ChipManager.addChip(new ChipPD43256BCZ());
        ChipManager.addChip(new Chip16bitOutputLCD());
        ChipManager.addChip(new Chip16bitInputLCD());

        new Handler();
    }

    public static void main(String[] args) {

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            System.err.println("Couldn't change system look & feel.");
        }

        new ChipSimulator();
    }

}
