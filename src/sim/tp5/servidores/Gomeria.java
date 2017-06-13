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
    
    public Gomeria(String nombre){
        super(nombre);
    }
    
    //private EmpleadoGomeria empleado1 = new EmpleadoGomeria();
    //private EmpleadoGomeria empleado2 = new EmpleadoGomeria();
    
//    @Override
//    public boolean estaLibre(){
//        return empleado1.estaLibre() || empleado2.estaLibre();
//    }
    
//    public EmpleadoGomeria getEmpleado(int empleado){
//        if (empleado == 1) return empleado1;
//        return empleado2;
//    }
  
//    @Override
//  public double iniciarAtencion(Cliente c,double reloj){
//         clienteActual = c;
//         c.actividadEnCola().atender(reloj);
//         Double tiempoDeAtencion = calcularTiempoAtencion();
//         if (empleado1.estaLibre()) empleado1.ocupar(c, tiempoDeAtencion, reloj);
//         else empleado2.ocupar(c, tiempoDeAtencion, reloj);
//         return tiempoDeAtencion;
//    }
    /*
    public double iniciarAtencion(Cliente c,double reloj){
        
         clienteActual = c;
         if (this.estaLibre()) this.estado = new EstadoServidor(EstadoServidor.OCUPADO);
         c.actividadEnCola().atender(reloj);
         return calcularTiempoAtencion();
    }
  
    public Cliente finalizar(double reloj){
        if(empleado1.getFinDeAtencion() == reloj) empleado1.liberar();
        else empleado2.liberar();        
        Cliente cli = clienteActual;
        this.clienteActual = null;
        return cli;
    }*/
  
  protected double calcularTiempoAtencion(){
      //Pasamos los valosres a segundos
      //No estoy seguro de que sean los límites a y b
    return Distribucion.generarUniforme(10,26); //En minutos
    }
}
