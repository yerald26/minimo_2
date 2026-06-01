package com.eetac.proyecto_dsa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalUserManager {

    private static final String PREFS_NAME          = "dungeon_users";
    private static final String KEY_LOGGED_EMAIL     = "logged_email";
    private static final String KEY_LOGGED_USERNAME  = "logged_username";
    private static final String KEY_LOGGED_COINS     = "logged_coins";
    private static final String KEY_LOGGED_INVENTORY = "logged_inventory";

    private SharedPreferences prefs;

    public LocalUserManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // -------------------------------------------------------
    // SESIÓN — guarda lo que devuelve la API tras el login
    // -------------------------------------------------------
    public void saveSession(String nombre) {
        prefs.edit()
                .putString(KEY_LOGGED_EMAIL, nombre)
                .putString(KEY_LOGGED_USERNAME, nombre)
                .apply();
    }

    // Versión completa con monedas e inventario
    public void saveSession(String email, String username, int coins, List<String> inventory) {
        String inventoryStr = TextUtils.join(",", inventory);
        prefs.edit()
                .putString(KEY_LOGGED_EMAIL, email)
                .putString(KEY_LOGGED_USERNAME, username)
                .putInt(KEY_LOGGED_COINS, coins)
                .putString(KEY_LOGGED_INVENTORY, inventoryStr)
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.contains(KEY_LOGGED_EMAIL);
    }

    public String getLoggedUsername() {
        return prefs.getString(KEY_LOGGED_USERNAME, "Héroe");
    }

    public void logout() {
        prefs.edit()
                .remove(KEY_LOGGED_EMAIL)
                .remove(KEY_LOGGED_USERNAME)
                .remove(KEY_LOGGED_COINS)
                .remove(KEY_LOGGED_INVENTORY)
                .apply();
    }

    // -------------------------------------------------------
    // MONEDAS
    // -------------------------------------------------------
    public int getCoins() {
        return prefs.getInt(KEY_LOGGED_COINS, 0);
    }

    public void updateCoins(int newCoins) {
        String email = prefs.getString(KEY_LOGGED_EMAIL, null);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_LOGGED_COINS, newCoins);
        if (email != null) {
            editor.putInt("coins_" + email, newCoins);
        }
        editor.apply();
    }

    // -------------------------------------------------------
    // INVENTARIO
    // -------------------------------------------------------
    public void añadirAlInventario(String nombreObjeto) {
        List<String> inventarioActual = obtenerInventario();
        inventarioActual.add(nombreObjeto);
        String inventoryStr = TextUtils.join(",", inventarioActual);

        prefs.edit()
                .putString(KEY_LOGGED_INVENTORY, inventoryStr)
                .apply();
    }

    public List<String> obtenerInventario() {
        String inventoryStr = prefs.getString(KEY_LOGGED_INVENTORY, "");
        if (inventoryStr.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(inventoryStr.split(",")));
    }
}