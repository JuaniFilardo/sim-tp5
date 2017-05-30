/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5;

import sim.tp5.estados.EstadoActividad;


/**
 *
 * @author filardo
 */
public class Actividad {
    
    public static final String SURTIDOR = "Surtidor";
    public static final String GOMERIA = "Gomeria";
    public static final String NEGOCIO = "Negocio";
    
    private String nombre;
    private EstadoActividad estado;
    private Double horaInicioEnCola, horaFinEnCola;
    
    public Actividad (String nombre, double reloj) {
        
        if (nombre.equalsIgnoreCase(SURTIDOR) || nombre.equalsIgnoreCase(GOMERIA) || nombre.equalsIgnoreCase(NEGOCIO)) {
            this.nombre = nombre;
        } else {
            this.nombre = "";
        }
        this.horaInicioEnCola = reloj;
        this.estado = new EstadoActividad(EstadoActividad.PENDIENTE);
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public double getHoraInicioCola(){
        return this.horaInicioEnCola;
    }
    
    public double getEspera(){
        return this.horaFinEnCola - this.horaInicioEnCola;
    }
    
    public boolean esPendiente(){
        return estado.esPendiente();
    }
    
    public boolean esEnCola(){
           return estado.esEnCola();
    }

    public boolean esSiendoAtendida(){
          return estado.esSiendoAtendida();
    }
    
    public boolean esFinalizada(){
           return estado.esFinalizada();
    }
    
    //Solo si es pendiente se puede encolar
    public void encolar(double reloj){
        if(estado.esPendiente()){
            this.horaInicioEnCola = reloj;
        }
        estado = new EstadoActividad(EstadoActividad.EN_COLA);
    }
    
    //Solo si está e cola puede passar a atendida
    public void atender(double reloj){
        if(estado.esEnCola() || estado.esPendiente()){
            this.horaFinEnCola = reloj;
        }
        estado = new EstadoActividad(EstadoActividad.SIENDO_ATENDIDO);
    }
    
    public void finalizar(){
        estado = new EstadoActividad(EstadoActividad.FINALIZADA);
    }
    
    @Override
    public String toString(){
        return this.nombre;
    }
}
