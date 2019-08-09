/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist.classes.core;

import org.wh.materials.core.CoreApplication;
import org.wh.materials.core.Settings;

import java.util.Locale;

/**
 * @author Wilson
 */
public class MySettings extends Settings {

    public MySettings() {
        super();
    }

    @Override
    public Object defaultSetting(String settingName) {
        switch (settingName) {
            case "appUsed":
            case "alwaysOnTop":
                return false;
            case "locale":
                return Locale.getDefault();
            case "savePath":
                return System.getProperty("user.home") + "\\Documents";
            case "saveFile":
                return "MyTaskList.tl";
            case "autosavePath":
                return CoreApplication.getBasePath() + "/autosave/tasklist.tl";
            case "showClosedDialog":
            case "editTaskOnCreate":
            case "restoreSession":
            case "openAppend":
                return true;
            case "colorId":
                return 0;
            case "windowHeight":
                return 400.0;
        }
        return null;
    }

}
