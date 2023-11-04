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
                jugadoresConectados.add(serv.accept());
                System.out.println("AAAAA");
                ventanaServer.mostrar("Jugador conectado");
                ventanaServer.mostrar(jugadoresConectados.size() + ": Es el total");
                threadsConectados.add(new threadServer(jugadoresConectados.get(jugadoresConectados.size() - 1), this, 1));
                
            }
           
            
            // espera segundo cliente
            //cliente2 = serv.accept();
            //ventana.mostrar(".::Segundo Cliente Aceptado");
            //threadServidor user2 = new threadServidor(cliente2, this,2);
            //user2.start();
            
            // 
            jugadoresConectados.get(0).contrincante1 = jugadoresConectados.get(1);
            //user2.enemigo = user1;
            
            
            
            
            //while (true)
            //{
            
            //}
            
        } catch (IOException ex) {
            ventanaServer.mostrar("ERROR ... en el servidor");
        }
    }
    
    
    
}
