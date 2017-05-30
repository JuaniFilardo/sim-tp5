/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

import sim.Distribucion;
import sim.tp5.Cliente;
import sim.tp5.estados.EstadoServidor;

/**
 *
 * @author filardo
 */
public class Gomeria extends Servidor {
    
    private EmpleadoGomeria empleado1;
    private EmpleadoGomeria empleado2;
    
    @Override
    public boolean estaLibre(){
        return empleado1.estaLibre() && empleado2.estaLibre();
    }
  
  public void iniciarAtencion(Cliente c,double reloj){
         clienteActual = c;
         c.actividadEnCola().atender(reloj);
         
         if (empleado1.estaLibre()) empleado1.ocupar();
         else empleado2.ocupar();
    }
  
  public Cliente finalizar(){
        if (!empleado1.estaLibre()) empleado1.librerar();
         else empleado2.librerar();
        Cliente cli = clienteActual;
        this.clienteActual = null;
        return cli;
    }
  
  protected double calcularTiempoAtencion(){
      //Pasamos los valosres a segundos
      //No estoy seguro de que sean los límites a y b
        return Distribucion.generarUniforme((10*60), (26*60));
    }
}
