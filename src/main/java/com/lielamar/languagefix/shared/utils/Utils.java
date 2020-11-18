package com.lielamar.languagefix.shared.utils;

import org.bukkit.Bukkit;

public class Utils {

    /**
     * Return a class based on the package name, the server version and the class name
     *
     * @param packageName   Package Name
     * @param className     Class Name
     * @return              Class object
     */
    public static Class<?> getClass(String packageName, String className) {
        String name = packageName + "." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + className;
        Class<?> nmsClass = null;

        try { nmsClass = Class.forName(name); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return nmsClass;
    }

}
