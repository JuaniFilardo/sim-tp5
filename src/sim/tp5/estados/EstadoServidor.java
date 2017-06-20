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
public class EstadoServidor {
    
    public static final int LIBRE = 0;
    public static final int OCUPADO = 1;
    public static final int PURGANDO = 5;
    
    private String estado;

    public EstadoServidor(int estado) {
        if (estado == LIBRE) this.estado = "Libre";
        if (estado == OCUPADO) this.estado = "Ocupado";
        if (estado == PURGANDO) this.estado = "Purgando";
        
    }
    
    public EstadoServidor(String estado) {
         this.estado = estado;
                
    }
    
    public String getEstado() {
        return this.estado;
    }
    
    public static String getEstado(int estado) {
        if (estado == LIBRE) return "Libre";
        if (estado == OCUPADO) return "Ocupado";
        if (estado == PURGANDO) return "Purgando";
        else return "nada wacho";
    }
}
