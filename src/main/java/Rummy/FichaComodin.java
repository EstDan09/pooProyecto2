/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rummy;

/**
 *
 * @author andy2
 */
public class FichaComodin extends Ficha{
    String simbolo;

    public FichaComodin(String simbolo, String color) {
        super(0, color);
        this.simbolo = simbolo;
    }

    @Override
    public String toString() {
        return "FichaComodin{" + "simbolo=" + simbolo + ", color=" + color + '}';
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    
    
}
