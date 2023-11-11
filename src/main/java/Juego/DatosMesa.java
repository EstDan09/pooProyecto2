/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Juego;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Esteban
 */
public class DatosMesa implements Serializable {
    static final long serialVersionUID = 1L;
    Color bgColor;
    int row;
    int col;

    public DatosMesa(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
    
    
}
