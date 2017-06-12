/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.servidores;

import sim.Distribucion;

/**
 *
 * @author filardo
 */
public class Surtidor extends Servidor {
    
    public Surtidor(String nombre){
        super(nombre);
    }
    
    protected double calcularTiempoAtencion(){
        return Distribucion.generarUniforme((45/60),(55/60)); //En minutos
    }
}
