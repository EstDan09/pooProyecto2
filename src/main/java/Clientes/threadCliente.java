/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clientes;

import Juego.DatosMesa;
import Rummy.Juego;
import java.io.DataInputStream;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Esteban
 */
public class threadCliente extends Thread {
    VentanaCliente ventadaDeCliente;
    DataInputStream entrada;
    DataOutputStream salida;
    InputStream entradaRaw;
    Juego juegoActivo;
    
    public threadCliente(DataInputStream entrada, DataOutputStream salida, VentanaCliente vcli) throws IOException {
        this.entrada = entrada;
        this.ventadaDeCliente = vcli;
        this.salida = salida;
    }

    public void setJuegoActivo(Juego juegoActivo) {
        this.juegoActivo = juegoActivo;
    }
    
    
    
    public void run() {
        //VARIABLES
        String jsonRec  = "";
        DatosMesa[][] mesaT;
        int opcion = 0;

        // solamente lee lo que el servidor threadServidor le envia
        while (true) {
            try {
                // esta leyendo siempre la instruccion, un int
                opcion = entrada.readInt();

                switch (opcion) {
                    case 1://caso para actualizar lista de usuarios conectados al server
                        
                        jsonRec = entrada.readUTF();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ArrayList<String> listaParaEnviar = objectMapper.readValue(jsonRec, ArrayList.class);
                        ventadaDeCliente.mostrarJugadores(listaParaEnviar);
                        break;
                        
                    case 2://caso para mostrar en la pantalla de todos los usarios los mensajes globales
                        System.out.println("hi2");
                        String mensajeRecibido = entrada.readUTF();
                        ventadaDeCliente.mostrarMensajes(mensajeRecibido);
                        break;
                    
                    case 3: //actualizar mesa
                        int row = entrada.readInt();
                        int col = entrada.readInt();
                        
                        String[][] recibido = new String[row][col];
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < col; j++) {
                                recibido[i][j] = entrada.readUTF();
                                System.out.println(recibido[i][j]);
                            }
                        }
                        
                        ventadaDeCliente.actualizaMesa(recibido);
                        break;
                    case 4: //actualizar 
                        
                        break;
                    case 5: //meter a jugador a mi partida
                        
                        String jugadorWillingToJoin = this.entrada.readUTF();
                        System.out.println("me llego que soy un pive"  + "y tengo que meter a " + jugadorWillingToJoin);
                        this.salida.writeInt(6);
                        this.salida.writeUTF(jugadorWillingToJoin);
                        break;
                    case 6:
                        System.out.println("ABOUT TO COME");
                        ventadaDeCliente.gameAsGuest();
                        
                        
                }
            } catch (IOException e) {
                System.out.println("Error en la comunicaci�n " + "Informaci�n para el usuario");
                break;
            }
        }
        System.out.println("se desconecto el servidor");
    }
    
    
    
}
