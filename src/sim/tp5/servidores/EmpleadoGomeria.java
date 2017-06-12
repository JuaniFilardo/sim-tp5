/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

import sim.tp5.Cliente;

/**
 *
 * @author fedeb
 */
public class EmpleadoGomeria {
    private static final String LIBRE = "libre";
    private static final String OCUPADO = "ocupado";
    
    private String estado;
    private Double finDeAtencion;
    private Cliente c;
    
    public EmpleadoGomeria(){
        estado = LIBRE;
        finDeAtencion = null;
        c = null;
    }
    
    public Double getFinDeAtencion(){
        return finDeAtencion;
    }
    
    public boolean estaLibre(){
        return this.estado.equalsIgnoreCase(LIBRE);
    }
    
    public void liberar(){
        if (!estaLibre()){
            this.estado = LIBRE;
            finDeAtencion = null;
        }
    }
    
    public void ocupar(Cliente c, Double tiempoDeAtencion, Double reloj){
        if (estaLibre()){
            this.estado = OCUPADO;
            finDeAtencion = tiempoDeAtencion + reloj;
        }
    }
    
}
