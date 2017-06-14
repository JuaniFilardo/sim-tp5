/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.estados;

/**
 *
 * @author filardo
 */
public class EstadoActividad {
    
    public static final int PENDIENTE = 0;
    public static final int EN_COLA = 1;
    public static final int SIENDO_ATENDIDO = 2;
    public static final int FINALIZADA = 3;
    
    
    private String nombre;
    
    public EstadoActividad (int nombre) {
       
        switch (nombre) {
            case PENDIENTE:
                this.nombre = "Pendiente";
                break;
            case EN_COLA:
                this.nombre = "EnCola";
                break;
            case SIENDO_ATENDIDO:
                this.nombre = "SiendoAtendido";
                break;    
            case FINALIZADA:
                this.nombre = "Finalizada";
                break;
            default:
                this.nombre = null;
                break;
        }
    }
   
    
    public boolean esPendiente(){
        return this.nombre.equalsIgnoreCase(estado(EstadoActividad.PENDIENTE));
    }
    
    public boolean esEnCola(){
        return this.nombre.equalsIgnoreCase(estado(EstadoActividad.EN_COLA));
    }

    public boolean esSiendoAtendida(){
        return this.nombre.equalsIgnoreCase(estado(EstadoActividad.SIENDO_ATENDIDO));
    }
    
    public boolean esFinalizada(){
        return this.nombre.equalsIgnoreCase(estado(EstadoActividad.FINALIZADA));
    }
    
 
    public String estado(int nombre){
           String est;
            switch (nombre) {
            case PENDIENTE:
                est = "Pendiente";
                break;
            case EN_COLA:
                est = "EnCola";
                break;
            case SIENDO_ATENDIDO:
                est = "SiendoAtendido";
                break;    
            case FINALIZADA:
                est = "Finalizada";
                break;
            default:
                est = null;
                break;
        }
        return est;
}

    public String getEstado(){
        return this.nombre;
    }
            
}
