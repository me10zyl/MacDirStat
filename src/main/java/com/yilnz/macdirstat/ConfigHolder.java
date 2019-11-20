package com.yilnz.macdirstat;

import java.util.ArrayList;
import java.util.List;

public class ConfigHolder {
    private List<String> favoriteDirs = new ArrayList<>();

    public ConfigHolder() {
        favoriteDirs.add("/Users/zyl/");
    }

    public List<String> getFavoriteDirs() {
        return favoriteDirs;
    }
}
