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
    
    private int porcentajeInestabilidadMaximo;
    private EstadoServidor estadoAnterior = new EstadoServidor(EstadoServidor.LIBRE);
    private double tiempoRemanencia;
    
    public Gomeria(String nombre){
        super(nombre);
    }
    
    public int getPorcentajeInestabilidadMax(){
        int porc = this.porcentajeInestabilidadMaximo;
        this.porcentajeInestabilidadMaximo = -1;
        return porc;
    }
    
    public void setPorcentajeInestabilidadMax(int porcInestabilidad){
        this.porcentajeInestabilidadMaximo = porcInestabilidad;
    }
    
    public void purgar(double tiempoRemanencia){
        if (!estaPurgando()) {
            estadoAnterior = new EstadoServidor(super.estado.getEstado());
            this.tiempoRemanencia = tiempoRemanencia;
            super.estado = new EstadoServidor(EstadoServidor.PURGANDO);
        }
    }
    
    public boolean estaPurgando(){
        return this.estado.getEstado().equalsIgnoreCase(EstadoServidor.getEstado(EstadoServidor.PURGANDO));
    }
    
    public Double terminarPurga(){
        super.estado = estadoAnterior;
        //estadoAnterior = null;
        return tiempoRemanencia;
    }
    
  protected double calcularTiempoAtencion(){
      //Pasamos los valosres a segundos
      //No estoy seguro de que sean los límites a y b
    return Distribucion.generarUniforme(1,3); //En minutos
    }
}
