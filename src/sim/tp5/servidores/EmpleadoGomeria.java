/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

/**
 *
 * @author fedeb
 */
public class EmpleadoGomeria {
    private static final String LIBRE = "libre";
    private static final String OCUPADO = "ocupado";
    
    private String estado;
    
    public EmpleadoGomeria(){
        estado = LIBRE;
    }
    
    public boolean estaLibre(){
        return this.estado.equalsIgnoreCase(LIBRE);
    }
    
    public void librerar(){
        if (!estaLibre()){
            this.estado = OCUPADO;
        }
    }
    
    public void ocupar(){
        if (estaLibre()){
            this.estado = LIBRE;
        }
    }
    
}
