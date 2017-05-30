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
    
    private String estado;
    
    public EstadoActividad (int estado) {
       
        switch (estado) {
            case PENDIENTE:
                this.estado = "Pendiente";
                break;
            case EN_COLA:
                this.estado = "EnCola";
                break;
            case SIENDO_ATENDIDO:
                this.estado = "SiendoAtendido";
                break;    
            case FINALIZADA:
                this.estado = "Finalizada";
                break;
            default:
                this.estado = null;
                break;
        }
    }
    
    public String getEstado() {
        return this.estado;
    }
}
