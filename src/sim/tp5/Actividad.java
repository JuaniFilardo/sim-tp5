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
    
    public Actividad (String nombre) {
        
        if (nombre.equalsIgnoreCase(SURTIDOR) || nombre.equalsIgnoreCase(GOMERIA) || nombre.equalsIgnoreCase(NEGOCIO)) {
            this.nombre = nombre;
        } else {
            this.nombre = "";
        }
    }
    
}
