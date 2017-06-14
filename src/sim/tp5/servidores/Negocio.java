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
public class Negocio extends Servidor {
    
    private Double horaInicioOcupacion = 0.0;
 
    public Negocio(String nombre){
        super(nombre);
    }
    
    protected double calcularTiempoAtencion(){
        return Distribucion.generarUniforme(1,5); //En minutos
    }
    
    public Double calcularOcupacion(Double reloj){
        return reloj - this.horaInicioOcupacion;
    }
    
    public void setHoraInicio(Double reloj){
        this.horaInicioOcupacion = reloj;
    }
    
    public Double getHoraInicio(){
        return this.horaInicioOcupacion;
    }
    
    /**
     * Método que procesa a un cliente, calculando el tiempo de atención y creando 
     * un evento FinDeAtención.
     * @param Cliente c -> Cliente que procesará el servidor
     *
     */
    public double iniciarAtencion(Cliente c,double reloj){
        horaInicioOcupacion = reloj;
         clienteActual = c;
         if (this.estaLibre()) this.estado = new EstadoServidor(EstadoServidor.OCUPADO);
         c.proximaActividad().atender(reloj);
         return calcularTiempoAtencion();
    }
    
     public double iniciarAtencionCola(Cliente c,double reloj){
         horaInicioOcupacion = reloj;
        clienteActual = c;
        if (this.estaLibre()) this.estado = new EstadoServidor(EstadoServidor.OCUPADO);
        
        c.actividadEnCola().atender(reloj);
        return calcularTiempoAtencion();
    }
    
    public Cliente finalizar(){
        this.horaInicioOcupacion = null;
        Cliente cli = clienteActual;
        this.clienteActual = null;
        return cli;
    }
    
}
