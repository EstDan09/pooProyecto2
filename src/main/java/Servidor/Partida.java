/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Esteban
 */
public class Partida {
    private ArrayList<threadServer> jugadoresEnPartida;
    private String name;
    private int turn;
    private int size = 0;
    private boolean accept = true;

    public ArrayList<threadServer> getJugadoresEnPartida() {
        return jugadoresEnPartida;
    }

    public void setJugadoresEnPartida(ArrayList<threadServer> jugadoresEnPartida) {
        this.jugadoresEnPartida = jugadoresEnPartida;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    

    public Partida(threadServer jugadorInicial) throws JsonProcessingException {
        this.name = jugadorInicial.getNameUser();
        this.jugadoresEnPartida = new ArrayList<>();
        jugadorInicial.setCurrentPartida(name);
        this.jugadoresEnPartida.add(jugadorInicial);
        this.name = jugadorInicial.getNameUser();
        this.size++;
        jugadorInicial.setInGameID(size);
        jugadorInicial.setStatusPlayer("Esperando (" + this.size + "/4)" );
        jugadorInicial.sendUser();
        System.out.println("Tamaño de partda de " + this.name + " es de " + this.size );
        
    }
    
    public int addJugador(threadServer jugadorToAdd) throws JsonProcessingException, IOException {
        if (accept) {
            if (this.size < 5) {
                jugadoresEnPartida.add(jugadorToAdd);
                this.size++;
                jugadorToAdd.setCurrentPartida(this.name);
                jugadorToAdd.setInGameID(size);
                jugadorToAdd.sendUser();
                jugadorToAdd.setStatusPlayer("Esperando en sala de " + this.name);
                jugadorToAdd.sendUser();
                System.out.println("Tamaño de partda de " + this.name + " es de " + this.size);
                jugadorToAdd.createGame();
                return 1;
            } else {
                System.out.println("Partida llena");
                return -1;
            }
        } else {
            System.out.println("Partida Iniciada");
            return -1;
        }

    }
    
    public void iniciar() throws JsonProcessingException {
        int id = 1;
        for (threadServer jugador : jugadoresEnPartida) {
            jugador.setStatusPlayer("En Partida");
            jugador.sendUser();
            jugador.setInGameID(id);
            id++;

        }
        System.out.println("adsadasdasdasdasdasd");
        System.out.println(this.size + this.turn);
        this.turn = 1;
        
    }
    
    
    
            
    
    
    
    
    
    
    
}
