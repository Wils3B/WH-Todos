/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist.classes;

import java.io.Serializable;

/**
 * @author Wilson
 */
public class Task implements Serializable {
    private int id;
    private long debut;
    private long fin;
    private String libelle;
    private boolean fini;
    private String details;

    public Task(int id, long debut, long fin, String libelle, boolean fini, String details) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.libelle = libelle;
        this.fini = fini;
        this.details = details;
    }

    public Task(int id, long debut, long fin, String libelle, boolean fini) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.libelle = libelle;
        this.fini = fini;
    }

    public Task(long debut, long fin, String libelle, boolean fini) {
        this.debut = debut;
        this.fin = fin;
        this.libelle = libelle;
        this.fini = fini;
    }

    public Task() {
        this(System.currentTimeMillis(), System.currentTimeMillis() + 3600000, "", false);
    }

    public long getDebut() {
        return debut;
    }

    public void setDebut(long debut) {
        this.debut = debut;
    }

    public long getFin() {
        return fin;
    }

    public void setFin(long fin) {
        this.fin = fin;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isFini() {
        return fini;
    }

    public void setFini(boolean fini) {
        this.fini = fini;
    }

    public int getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
