package com.myleakyconlondon.util;

import com.myleakyconlondon.ui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Elizabeth
 * Date: 15/06/13
 * Time: 09:54
 */
public class LeakyConConstants {


    public static Map<String, Integer> getColours() {
        Map<String, Integer> colours = new HashMap<String, Integer>();
        colours.put("Panel", R.color.LightPeaGreen);
        colours.put("Show", R.color.LightBlueViolet);
        colours.put("Ball", R.color.LightOrange);
        colours.put("Wrock", R.color.LightOrange);
        colours.put("Special Event", R.color.Gold);
        colours.put("Autographs", R.color.LightRed);
        colours.put("Meet-up", R.color.Pink);
        colours.put("Discussion", R.color.LightBlue);
        colours.put("Workshop", R.color.CyanBlue);
        colours.put("Vending", R.color.LightYellow);
        colours.put("Other", R.color.Cream);
        colours.put("Food", R.color.LightCyanBlue);

        return colours;
    }
}