package com.eetac.proyecto_dsa.model;

public class InscripcionRequest {
    private String username;
    private String idEvento;

    // Constructor vacío requerido por Gson
    public InscripcionRequest() {
    }

    // Constructor para facilitar la creación del objeto
    public InscripcionRequest(String username, String idEvento) {
        this.username = username;
        this.idEvento = idEvento;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
}