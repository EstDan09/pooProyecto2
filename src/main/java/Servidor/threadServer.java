/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Juego.DatosMesa;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    ArrayList<threadServer> jugadoresEnLinea;

    public threadServer(Socket clienteOwner, Server servidor, int num) {
        this.cliente = clienteOwner;
        this.servidor = servidor;
        this.numeroDeJugador = num;
        jugadoresEnLinea = new ArrayList<>();
        nameUser = "por mientras";
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String name) {
        nameUser = name;
    }
    
    public void refresherToJugadoresEnLinea (ArrayList<threadServer> jugadoresActualizados) {
        jugadoresEnLinea = jugadoresActualizados;
        System.out.println("Jugador Agregado");
    }

    public void run() {
        try {
            // inicializa las comunicaciones
            entrada = new DataInputStream(cliente.getInputStream());//comunic
            salida = new DataOutputStream(cliente.getOutputStream());//comunic
            this.setNameUser(entrada.readUTF());
            System.out.println("Hola soy una nueva conexión de nombre " + getNameUser());

            sendUser();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        int caso = 0;
        while (true) {
            try {
                //Siempre espera leer un int que será la instruccion por hacer
                caso = entrada.readInt();
                switch (caso) {
                    case 1:
                        
                        break;
                    case 2:
                        System.out.println("step1");
                        //Recibo lo que un jugador me manda
                        String mensajeRecibido = entrada.readUTF();
                        
                        //Mando a todos el mensaje
                        for (threadServer jugador: jugadoresEnLinea) {
                            jugador.salida.writeInt(2);
                            jugador.salida.writeUTF(mensajeRecibido);
                        }
                        break;
                    case 3:
                        System.out.println("ok");
                        
                        int row = entrada.readInt();
                        int col = entrada.readInt();
                        String[][] recibido = new String[row][col];
                        
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < col; j++) {
                                recibido[i][j] = entrada.readUTF();
                                System.out.println(recibido[i][j]);
                            }
                        }
                        
                        for (threadServer jugador : jugadoresEnLinea) {
                            jugador.salida.writeInt(3);
                            jugador.salida.writeInt(row);
                            jugador.salida.writeInt(col);
                            for (String[] rows : recibido) {
                                for (String code : rows) {
                                    jugador.salida.writeUTF(code);
                                }
                            }
                        }

                        break;

                    case 4:
                        
                        break;
                    case 5:
                        
                        break;
                    case 6:
                        
                        break;
                    case 7:
                        
                        break;
                }
            } catch (IOException e) {
                System.out.println("El cliente termino la conexion");
                break;
            }
        }
            
    }
    
    public void sendUser() throws JsonProcessingException {
        
        for (threadServer jugador: jugadoresEnLinea) {
            ArrayList<String> listaParaEnviar = new ArrayList <>();
            for (threadServer jugadorU: jugadoresEnLinea) {
                listaParaEnviar.add(jugadorU.getNameUser());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(listaParaEnviar);
            
            try {
                jugador.salida.writeInt(1);
                jugador.salida.writeUTF(json);	
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
