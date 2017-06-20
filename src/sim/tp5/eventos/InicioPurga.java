/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.eventos;

/**
 *
 * @author fedeb
 */
public class InicioPurga extends Evento{
    
    public InicioPurga(Double hora){
        super(Evento.INICIO_PURGA, hora);
    }
    
}
