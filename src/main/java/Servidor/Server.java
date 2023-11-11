/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Esteban
 */
public class Server {
    VentanaServidor ventanaServer;
    ArrayList<Socket> jugadoresConectados;
    ArrayList<threadServer> threadsConectados;
    int contadorJugadores = 1;
    

    public Server(VentanaServidor main) {
        this.ventanaServer = main;
        jugadoresConectados = new ArrayList<>();
        threadsConectados = new ArrayList<>();
    }
    
    public void runServer()
    {
        try {
            //crea el socket servidor para aceptar dos conexiones
            ServerSocket serv = new ServerSocket(8082);
            ventanaServer.mostrar(serv.toString());
            ventanaServer.mostrar("-Estamos Activos");
            ventanaServer.mostrar("-Esperando jugadores...");
            
            // espera primer cliente
            while (true)
            {
                Socket jugador = serv.accept();
                jugadoresConectados.add(jugador);
                ventanaServer.mostrar("Jugador conectado");
                ventanaServer.mostrar(jugadoresConectados.size() + ": Es el total");
                threadServer threadServerJugador = new threadServer (jugador, this, contadorJugadores);
                threadsConectados.add(threadServerJugador);
                for (threadServer threadDisp: threadsConectados) {
                    ArrayList<threadServer> temporal = new ArrayList<>();
                    for (threadServer threadDispU: threadsConectados) {
                        temporal.add(threadDispU);
                    }
                    threadDisp.refresherToJugadoresEnLinea(temporal);
                }
                threadServerJugador.start();            
            }
            
        } catch (IOException ex) {
            ventanaServer.mostrar("ERROR ... en el servidor");
        }
    }
    
    
    
}
