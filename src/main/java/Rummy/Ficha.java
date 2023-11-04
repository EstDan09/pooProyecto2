/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rummy;

/**
 *
 * @author andy2
 */
public class Ficha {
    private int numero;
    String color;

    public Ficha(int numero, String color) {
        this.numero = numero;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Ficha{" + "numero=" + numero + ", color=" + color + "}\n";
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    
    
    
}
