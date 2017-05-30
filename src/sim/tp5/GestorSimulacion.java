/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5;

/**
 *
 * @author filardo
 */
public class GestorSimulacion {
    
    
    
    /**
     * 
     * @param random Un número aleatorio entre 0 y 1
     * @return true si es random es menor a 0.80, false si no
     */
    private boolean cargaCombustible(double random) {
        return (random < 0.80);
    }
    
    /**
     * 
     * @param random Un número aleatorio entre 0 y 1
     * @return "Negocio" si es menor a 0.40; "Gomeria" si es mayor
     */
    private String obtenerActividadSiNoCarga(double random) {
        return (random < 0.40) ? Actividad.NEGOCIO : Actividad.GOMERIA;
    }
    
    /**
     * 
     * @param random Un número aleatorio entre 0 y 1
     * @return "Negocio" si es menor a 0.30; "Gomeria" si
     * 0.30 <= random < 0.50; null si es mayor a 0.50
     */
    private String obtenerActividadSiCarga(double random) {
      
        if (random < 0.50) {
            return (random < 0.30) ? Actividad.NEGOCIO : Actividad.GOMERIA;
        } else {
            return null;
        }       
    }
 
    
    public void simular(int cantSimulaciones) {
        
        // Primera iteración
            // Setear primera fila
            
        // Arranca desde la segunda
        for (int i = 1; i < cantSimulaciones; i++) {
            
            /*  calcular próximo evento
                seteo reloj con hora del próximo evento
                if (llegadaCliente):
                    calcularProxLlegada
                    cargarActividadesDeCliente
                    
                    switch (actividad):
                        evaluarEstadoServidores
                        if (libre):
                            inicioAtencion
                        else:
                            cola++
                elseif (finAtencion):
                    registrarFinActividad
                    getProximaActividad
                    -- servidor pregunta si tiene cola
                    if (tieneCola):
                        sacaDeLaCola
                        -- calculos
                        inicioAtencion -> crea el finAtencion
                    else:
                        pasarAEstadoLibre
            
                -- calcular variables estadísticas
                -- recorrer clientes
                -- actualizar campos
                -- todo lo que no se cambió lo copio de arriba
            */
        }
    }
}
