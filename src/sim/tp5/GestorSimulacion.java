/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.tp5;

import java.util.ArrayList;
import sim.tp5.eventos.Evento;
import sim.tp5.servidores.Heap;
import sim.tp5.servidores.Servidor;
import sim.Distribucion;
import sim.tp5.eventos.FinAtencion;
import sim.tp5.eventos.LlegadaCliente;

/**
 *
 * @author filardo
 */


/* ¡FALTA!
    
    Agregar las variables que van a la tabla, están los valores calculados en muchos casos,
    solo hay que asignar
    Al final de cada iteración, habría que recorrer los eventos de tipo FIN de ATENCIÓN para
    colocar en la tabla en la columan Fin de Atención de cada servidor el momento en que se producirá el evento
    (es solo para visualización, no sirve ara nada mas)
    Corrregir los límites para cuando se genera el tiempo de atención (en los servidores), no tengo el enunciado
    Pantalla para visualizar los datos
    Probar, que seguro hay que pulir una banda

    Cualquier cosa preguntenme, no sigo porque estoy re duro, lo último que hice seguro fue cualquiera jaja

*/


public class GestorSimulacion {
    
    Servidor gomeria, negocio, surtidor1, surtidor2, surtidor3;
    //Próximos eventos. El heap nos asegura de obtener el próximo
    private Heap<Evento> eventos;
    
    //Datos del ejercicio
    int media = 24/60; //En minutos
    int varianza = 23/60; //En minutos
    
    
    //VARIABLES DE TABLA
    //Reloj del sistema
    double reloj;
    //Eventos
    Double rndProxLlegada = null;
    Double tiempoEntreLlegadas = null;
    Double proximaLlegada = null;
    //Para ver que actividades realiza el cliente
    Boolean _combustible = false;
    Boolean _negocio = false;
    Boolean _gomeria = false;
    //RND
    Double rndActividadSiCarga = 0.0;
    Double rndActividadSiNoCarga = 0.0;
    
    //Se declaran TODAS las variables que compete la fila
    Evento evt = null;


 //Acumula clientes que pasaron por esa cola
    Double acumTiempoEnColaSurtidor = 0.0;
    Double acumTiempoEnColaGomeria = 0.0;
    Double acumTiempoEnColaNegocio = 0.0;
    //Cuenta ls clientes que pasaron por esa cola
    int contColaSurtidor = 0;
    int contColaGomeria = 0;
    int contColaNegocio = 0;
        
        
        
    
    //Varible que se utiliza para guardar el segundo valor que nos devuelve el generador
    //Si la variable está en null, se generan los números. Si no, se usa el valor de la variable
    Double normalCalculado = null;
    
    public GestorSimulacion(){
        
        //En primer lugar se inicializan los servidores
        gomeria = new Servidor();
        negocio = new Servidor();
        surtidor1 = new Servidor();
        surtidor2 = new Servidor();
        surtidor3 = new Servidor();
        
        //Inicializamos el heap de eventos
        eventos = new Heap(10);
        
        //Incializamos reloj en 0;
        reloj = 0;
    }
    
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
    
    
 
    /**
     * Se calcula el tiempo entre llegadas de los clientes. 
     * Distribución Normal!
     * @return Double que refleja el tiempo para la siguiente llegada
     */
    private double calcularTiempoEntreLlegadas(){
        double tiempoEntreLlegadas = 0;
        if (normalCalculado == null){
            double numNorm[] = Distribucion.generarNormal(media, varianza);
            tiempoEntreLlegadas = numNorm[0];
            normalCalculado = numNorm[1];
        }
        else {
            tiempoEntreLlegadas = normalCalculado;
            normalCalculado = null;
        }
        return tiempoEntreLlegadas;
    }
    
    private Cliente crearCliente(){
        Cliente nuevo = new Cliente();
        if (cargaCombustible(Math.random())){
            nuevo.agregarActividad(new Actividad(Actividad.SURTIDOR, reloj));
            rndActividadSiCarga = Math.random();
            String act = obtenerActividadSiCarga(rndActividadSiCarga);
            nuevo.agregarActividad(new Actividad(act, reloj));
            this._combustible = true;
            if (act.equalsIgnoreCase(Actividad.GOMERIA)) this._gomeria = true;
            else this._negocio = true;
        }
        else {
            rndActividadSiNoCarga = Math.random();
            String act = obtenerActividadSiCarga(rndActividadSiNoCarga);
            nuevo.agregarActividad(new Actividad(obtenerActividadSiNoCarga(Math.random()),reloj));
            if (act.equalsIgnoreCase(Actividad.GOMERIA)) this._gomeria = true;
            else this._negocio = true;
        }
        return nuevo;
    }
    
    private void asignarClienteAServidor(Cliente c){
        if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.SURTIDOR)){
            Servidor candidato = surtidorLibre();
            if (candidato != null){
                candidato.iniciarAtencion(c, reloj);
            }
            else {
                candidato = surtidorConMenorCola();
                candidato.addCola(c, reloj);
            }
        }
        else if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.GOMERIA)){
            if (gomeria.estaLibre()){
                gomeria.iniciarAtencion(c, reloj);
                //Que empleado se ocupa?
            }
            else {
                gomeria.addCola(c, reloj);
            }
        }
        else if (c.proximaActividad().getNombre().equalsIgnoreCase(Actividad.NEGOCIO)){
            if (negocio.estaLibre()){
                negocio.iniciarAtencion(c, reloj);
            }
            else negocio.addCola(c, reloj);
        }
    }
    
    private Servidor surtidorLibre(){
        if (surtidor1.estaLibre()){
            return surtidor1;
        }
        else if(surtidor2.estaLibre()){
            return surtidor2;
        }
        else if(surtidor3.estaLibre()){
            return surtidor3;
        }
        else return null;
    }
    
    private Servidor surtidorConMenorCola(){
        if (surtidor1.getTamañoCola() <= surtidor2.getTamañoCola()){
            if (surtidor1.getTamañoCola() <= surtidor3.getTamañoCola()) return surtidor1;
            else return surtidor3;
        }
        else if (surtidor2.getTamañoCola() <= surtidor3.getTamañoCola())return surtidor2;
        else return surtidor3;
    }
    
    
    private void primeraIteracion(){
        reloj = 0;
        rndProxLlegada = null;
        tiempoEntreLlegadas = null;
        proximaLlegada = null;
        _combustible = null;
        _negocio = null;
        _gomeria = null;
        rndActividadSiCarga = null;
        rndActividadSiNoCarga = null;
        Evento evt = null;
         acumTiempoEnColaSurtidor = 0.0;
         acumTiempoEnColaGomeria = 0.0;
         acumTiempoEnColaNegocio = 0.0;
         contColaSurtidor = 0;
         contColaGomeria = 0;
         contColaNegocio = 0;
        //FALTA
        
    }
    
    public void simular(int cantSimulaciones) {
        // Primera iteración
            // Setear primera fila
        primeraIteracion();
        // Arranca desde la segunda
        for (int i = 1; i < cantSimulaciones; i++) {
            
            //  calcular próximo evento
            //Se obtiene y se borra el elemento de la cima del heap
            evt = this.eventos.remove();
            // Setear reloj según el evento que ocurre
            reloj = evt.getHora();
            
            if (evt.getTipo().equalsIgnoreCase(Evento.LLEGADA_CLIENTE)){
                //Calcular próxima llegada
                tiempoEntreLlegadas = calcularTiempoEntreLlegadas();
                proximaLlegada = reloj + tiempoEntreLlegadas;
                //se crea evento para la próxima llegada y se lo agrega al heap
                eventos.add(new LlegadaCliente(proximaLlegada));
                //Se crea el cliente que llegó y se llena la lista de actividades que 
                //quiere realizar
                Cliente c = crearCliente();
                asignarClienteAServidor(c);
            }
            else if (evt.getTipo().equalsIgnoreCase(Evento.FIN_ATENCION)){
                FinAtencion fa = (FinAtencion) evt;
                //Referencio al servidor que termino la atención
                Servidor target = fa.getServidor();
                //Finaliza la atención del servidor!!
                Cliente atendido = target.finalizar();
                //Se calcula el contador y el acumulador de la variable que corresponda
                calcularVariablesEstadisticas(atendido);
                //Cambio de estado a la actividad del cliente
                atendido.actividadSiendoAtendida().finalizar();
                //Pregunto si el cliente tiene agluna otra actividad por realizar
                Actividad proxActividad = atendido.proximaActividad();
                if (proxActividad != null){
                    asignarClienteAServidor(atendido);
                }
                double tiempoAtencion = target.atenderCola(reloj);     
                //Se agrega el evento de fin de atención 
                eventos.add(new Evento(Evento.FIN_ATENCION, (reloj+tiempoAtencion)));
                
            }
            //Faltan las demas lineas de la tabla
        }
        
        //calcularPromedioDeEsperaEnCola()
        //calcularPorcentajeOcupacion
        
    }
    
    private void calcularVariablesEstadisticas(Cliente cliente){
        //Se pregunta por el que está siendo atendido ya que todavía no se finalizó la actividad
        Actividad atendida = cliente.actividadSiendoAtendida();
        if (atendida.getNombre().equalsIgnoreCase(Actividad.SURTIDOR)){
            //Me parece que esto no anda xq el getEspera necesita horaFin para calcularse
            this.acumTiempoEnColaSurtidor += atendida.getEspera();
            this.contColaSurtidor++;
        }
        else if (atendida.getNombre().equalsIgnoreCase(Actividad.GOMERIA)){
            this.acumTiempoEnColaGomeria += atendida.getEspera();
            this.contColaGomeria++;
        }
        else if (atendida.getNombre().equalsIgnoreCase(Actividad.NEGOCIO)){
            this.acumTiempoEnColaNegocio += atendida.getEspera();
            this.contColaNegocio++;
        }
    }
}
