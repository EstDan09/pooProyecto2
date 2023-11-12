/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rummy;

/**
 *
 * @author andy2
 */
public class Jugada {
    private Ficha ficha;
    private int fila = -1;
    private int columna = -1;
    private int FichasJugadorX;
    private int FichasJugadorY; 

    public Jugada(Ficha ficha, int FichasJugadorX, int FichasJugadorY) {
        this.ficha = ficha;
        this.FichasJugadorX = FichasJugadorX;
        this.FichasJugadorY = FichasJugadorY;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getFichasJugadorX() {
        return FichasJugadorX;
    }

    public int getFichasJugadorY() {
        return FichasJugadorY;
    }

    @Override
    public String toString() {
        return "Jugada{" + "ficha=" + ficha + ", fila=" + fila + ", columna=" + columna + '}';
    }
    
    
    
}
