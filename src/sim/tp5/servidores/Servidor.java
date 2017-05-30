/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

import sim.tp5.estados.EstadoServidor;
import java.util.ArrayList;
import sim.tp5.Cliente;

/**
 *
 * @author filardo
 */
public abstract class Servidor {
  
    private ArrayList<Cliente> cola;
    private EstadoServidor estado;
    
}
