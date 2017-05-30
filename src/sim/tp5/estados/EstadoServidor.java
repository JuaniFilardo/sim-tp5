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
    
    private String estado;

    public EstadoServidor(int estado) {
        this.estado = (estado == LIBRE) ? "Libre" : "Ocupado";
    }
    
    public String getEstado() {
        return this.estado;
    } 
}
