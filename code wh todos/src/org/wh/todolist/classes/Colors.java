package org.wh.todolist.classes;

import java.util.Arrays;

public enum Colors {
    BLUE,
    BLUE2,
    CYAN,
    GOLDENRODYELLOW,
    GREEN,
    PINK,
    ROSE,
    SALMON,
    SEAGREEN,
    SLATEGREY,
    STEELBLUE,
    WHITE,
    YELLOW,
    YELLOW2;

    public String getName() {
        switch (this) {
            case BLUE:
                return "blue";
            case BLUE2:
                return "blue2";
            case CYAN:
                return "cyan";
            case GOLDENRODYELLOW:
                return "goldenrowyellow";
            case GREEN:
                return "green";
            case PINK:
                return "pink";
            case ROSE:
                return "rose";
            case SALMON:
                return "salmon";
            case SEAGREEN:
                return "seagreen";
            case SLATEGREY:
                return "slategrey";
            case STEELBLUE:
                return "steelblue";
            case WHITE:
                return "white";
            case YELLOW:
                return "yellow";
            case YELLOW2:
                return "yellow2";
        }
        return "blue";
    }

    public static Colors getRandomly() {
        Colors[] colors = Colors.values();
        System.out.println("LES COLORS : " + Arrays.toString(colors));
        int sel = (int) (colors.length * Math.random());
        return colors[sel];
    }
}
