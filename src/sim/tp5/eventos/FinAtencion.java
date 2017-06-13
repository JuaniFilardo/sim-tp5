/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5.eventos;

import sim.tp5.servidores.Servidor;

/**
 *
 * @author fedeb
 */
public class FinAtencion extends Evento {
    
    private Servidor servidor;
    
    public FinAtencion(Double hora, Servidor server) {
        super(Evento.FIN_ATENCION, hora);
        this.servidor = server;
    }
    
    public Servidor getServidor(){
        return this.servidor;
    }



}
