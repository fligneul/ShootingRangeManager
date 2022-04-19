package com.fligneul.srm.ui;

import javafx.scene.paint.Paint;

import java.time.format.DateTimeFormatter;

public class ShootingRangeManagerConstants {
    public static final String EMPTY = "";

    public static final String EMPTY_HYPHEN = "-";

    public static final String SPACE = " ";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static final Paint COLOR_GREY = Paint.valueOf("#757575");

    public static final Paint COLOR_RED = Paint.valueOf("#e53935");

    public static final String DOOR_FA_ICON = "fas-door-open";

    public static final String EDIT_FA_ICON = "fas-edit";

    public static final String TRASH_FA_ICON = "fas-trash-alt";

}
