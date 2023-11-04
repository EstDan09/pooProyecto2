/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Esteban
 */
public class threadServer extends Thread {

    Socket cliente = null;//referencia a socket de comunicacion de cliente
    DataInputStream entrada = null;//Para leer comunicacion
    DataOutputStream salida = null;//Para enviar comunicacion	
    String nameUser; //Para el nombre del usuario de esta conexion
    Server servidor;// referencia al servidor
    int numeroDeJugador; //ID de jugador en el server global
    threadServer contrincante1 = null;
    threadServer contrincante2 = null;
    threadServer contrincante3 = null;

    public threadServer(Socket clienteOwner, Server servidor, int num) {
        this.cliente = clienteOwner;
        this.servidor = servidor;
        this.numeroDeJugador = num;
        nameUser = "";// inicialmente se desconoce, hasta el primer read del hilo
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String name) {
        nameUser = name;
    }

    public void run() {
        try {
            // inicializa para lectura y escritura con stream de cliente
            entrada = new DataInputStream(cliente.getInputStream());//comunic
            salida = new DataOutputStream(cliente.getOutputStream());//comunic
            
            // Es el primer read que hace, para el nombre del user
            System.out.println("lee el nombre");
            this.setNameUser(entrada.readUTF());
            System.out.println("1. Leyo nombre: " + nameUser);
            //enviaUser(); // envia su nombre al otro usuario, que es con un 2
            // al enemigo
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
   

}
