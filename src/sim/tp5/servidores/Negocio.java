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
public class Negocio extends Servidor {
 
    protected double calcularTiempoAtencion(){
        return Distribucion.generarUniforme((2*60), (3*60));
    }
    
}
