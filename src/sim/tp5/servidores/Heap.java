package sim.tp5.servidores;

/**
 * Implementa el concepto de Heap o Monticulo Binario, mediante generics.  El Heap puede ser 
 * ascendente, en cuyo caso esta preparado para obtener en forma rapida el menor elemento del conjunto, 
 * o puede ser descendente, en cuyo caso esta preparado para obtener en forma rapida el mayor. Se puede 
 * crear un Heap ascendente enviando al constructor de la clase el valor true como segundo parametro, o 
 * crear un Heap descendente enviando un false a ese mismo constructor:
 * 
 *      Heap <Integer> ah = new Heap<>(10, true);  // un heap ascendente con capacidad inicial = 10
 *      Heap <Integer> dh = new Heap<>(false);     // un heap descendente con capacidad inicial = 100 (por default)
 *      
 * La capacidad del Heap ira variando a medida que se requiera. Si la capacidad no alcanza para una nueva
 * insercion, entonces la capacidad sera duplicada automaticamente. Si al realizar extracciones se 
 * detecta que el espacio ocupado es inferior al 90% de la mitad de la capacidad actual, entonces la 
 * capacidad sera automaticamente disminuida a la mitad (siempre que la nueva capacidad no sea inferior
 * a la capacidad con que originalmente fue creado el heap).
 * 
 * @author Ing. Frittelli - Catedra de TSB.
 * @version Octubre de 2013.
 */
public class Heap <T extends Comparable>
{      
      private Slot<T> heap[];          // el arreglo para representar al arbol.
      private int initial_capacity;    // la capacidad inicial del arreglo.
      private int count;               // la cantidad de elementos que tiene el Heap.
      private boolean ascendent;       // true: ascendente  -  false: descendente.
    
      /**
       * Crea un Heap vacio, con capacidad inicial para 100 componentes, orientado a 
       * la obtencion rapida del menor de n elementos (un heap ascendente: el menor en la raiz).
       */
      public Heap()
      {
          this(100, true);
      }
      
      /**
       * Crea un Heap vacio, con capacidad inicial para n componentes, orientado a 
       * la obtencion rapida del menor de esos elementos (un heap ascendente: el menor 
       * en la raiz).
       * @param n la capacidad inicial del heap.
       */
      public Heap(int n)
      {
          this(n, true);
      }
      
      /**
       * Crea un Heap vacio, con capacidad inicial para 100 componentes. El tipo de heap 
       * a crear depende del parametro t: si t = true, el heap ser� ascendente (orientado a la 
       * obtencion rapida del menor), si t = false entonces sera descendente (orientado a 
       * la obtencion rapida del mayor)..
       * @param t el tipo de heap a crear (true: ascendente - false: descendente)
       */
      public Heap(boolean t)
      {
          this(100, t);
      }      
      
      /**
       * Crea un Heap vacio, con capacidad inicial para n componentes. El tipo de heap a 
       * crear depende del parametro t: si t = true, el heap ser� ascendente (orientado a la 
       * obtencion rapida del menor), si t = false entonces sera descendente (orientado a 
       * la obtencion rapida del mayor).
       * @param n la capacidad inicial del heap.
       * @param t el tipo de heap a crear (true: ascendente - false: descendente)
       */
      public Heap(int n, boolean t)
      {
          int th = n;
          if(th <= 0) { th = 100; }
          heap = new Slot[th];
          initial_capacity = th;
          count = 0;
          ascendent = t;
      }
             
      /**
       * Inserta un nuevo objeto en el heap. Se admiten repeticiones. La insercion se realiza
       * con tiempo de ejecucion de O(log(n)), siendo n la cantidad de elementos del heap.
       * @param data el objeto a insertar.
       */
      public void add(T data)
      {
          int n = heap.length;
          if(count == n) { adjustCapacity(2*n); }
          
          int s = count;
          heap[s] = new Slot<>(data);
          while(s != 0)
          {
              int f = (s - 1) / 2;
              
              if( ! valid_change(s, f) ) { break; }
              
              Slot <T> aux = heap[s];
              heap[s] = heap[f];
              heap[f] = aux;
              s = f;
          }

          count++;
      }
    
      /**
       * Remueve todos los elementos del heap y lo deja vacio. La capacidad vuelve
       * al valor con el que inicialmente se creo el heap. El tipo de heap (ascendente 
       * o descendente) se mantiene igual al que se tenia antes de invocar a clear.  
       */
      public void clear()
      {
          heap = new Slot[initial_capacity];
          count = 0;
      }
      
      /**
       * Retorna el elemento de la cima del Heap, sin removerlo. Si el Heap
       * es de tipo ascendente, el objeto retornado sera el menor del heap,
       * y si el heap es descendente, el objeto retornado sera el mayor. Si 
       * el heap esta vacio, retorna null;
       */
      public T get()
      {
          if(isEmpty()) { return null; }
          return heap[0].getData();
      }
            
      /**
       * Permite determinar si el Heap esta vacio.
       * @return true si el Heap esta vacio.
       */
      public boolean isEmpty()
      {
          return (count == 0);    
      }
          
      /**
       * Obtiene y retorna el elemento de la cima del heap. Si el heap es de 
       * tipo ascendente, ese elemento sera el menor del heap. En caso contrario 
       * sera el mayor. Rearma el heap con los elementos restantes, de forma que
       * luego de terminada la operacion, la cima vuelve a contener al menor (o al
       * mayor) de los elementos que quedaban, y la cantidad de elementos se reduce
       * en uno. Si el heap esta vacio, retorna null. Tiempo de ejecuci�n esperado
       * (en el peor caso): O(log(n)).
       * @return el elemento menor (o el mayor) del heap.
       */
      public T remove()
      {
          if(isEmpty()) { return null; }
          
          T x = heap[0].getData();
          
          heap[0] = heap[count - 1]; 
          count--;
          
          // si sobra demasiado lugar, achicar el vector...
          int n = heap.length;
          int ic = initial_capacity;
          if(n/2 >= ic && count > ic && count < (n/2) * 0.9) { adjustCapacity(n/2); }

          int f = 0;
          while(f < count)
          {
              // calcular direcciones del hijo izquierdo (sl) y el derecho (sr)...
              int sl = f * 2 + 1;
              int sr = sl + 1;
              
              // si no hay hijos, cortar y terminar...
              if(sl >= count) { break; }

              // ...guardar en s el indice del hijo por el que se debe bajar... 
              int s = sl;
              if(sr < count && !optimal_left(sl, sr) ) { s = sr; }
              
              // si ese hijo no debe cambiar de lugar con el padre, cortar y terminar...
              if( ! valid_change(s, f) ) { break; }
              
              // ... caso contrario, intercambiar padre (heap[f]) con el hijo correcto (heap[s])...
                 Slot <T> aux = heap[f];
                 heap[f] = heap[s];
                 heap[s] = aux;         
              
              // ...seguir desde el hijo...
              f = s;
          }
          
          // ... y devolver el elemento que estaba originalmente en la cima...
          return x;
      }

      
      /**
       * Retorna la cantidad de elementos contenidos en el Heap.
       * @return el tama�o del Heap.
       */
      public int size()
      {
          return count;
      }
      
      /**
       * Retorna una cadena con la conversion a String del Heap, en orden  
       * lineal tal como figuran los elementos en el arreglo de soporte.
       * @return la conversion a String del Heap.
       */
      @Override
      public String toString()
      {
          if(isEmpty()) { return "[]"; }
            
          StringBuilder r = new StringBuilder("[ ");
          for(int i = 0; i < count; i++)
          {
              r.append(heap[i].toString());
              if(i != count - 1) { r.append(", "); }
          }
          r.append(" ]");
          return r.toString();  
      }
      
      /**
       * Retorna el tipo de Heap: true si es ascendente, false si es descendente.
       * @return true: el heap es ascendente - false: es descendente.
       */
      public boolean isAscendent()
      {
          return ascendent;    
      }
      
      /*
       * Ajusta la capacidad del arreglo al nuevo valor nc. Este valor puede ser
       * menor o mayor al anterior valor de capacidad. El metodo no controla si 
       * la operacion es valida para la cantidad de elementos en el heap original.
       */
      private void adjustCapacity( int nc )
      {
          int n = heap.length;          
          
          // cuantos elementos debo trasladar?
          int cant = n;
          if(nc < n) { cant = count; }
          
          // hacer el traslado...
          Slot <T> newheap[] = new Slot[nc];
          System.arraycopy(heap, 0, newheap, 0, cant);
          heap = newheap;
      }
            
      /* 
       * Chequea dos elementos hermanos y retorna true si el optimo es el izquierdo, o false si es
       * el derecho. Entendemos por "optimo" al menor de los dos si el heap es ascendente, o al mayor
       * si el heap es descendente.
       */
      private boolean optimal_left(int sl, int sr)
      {          
          int r = heap[sl].getData().compareTo(heap[sr].getData());
          
          // si el heap es ascendente y el izquierdo es menor que el derecho, salir con true...
          if( ascendent && r < 0 ) { return true; }

          // si el heap es descendente y el izquierdo es mayor que el derecho, salir con true...
          if( ! ascendent && r > 0) { return true; }
          
          // el optimo no es el izquierdo...
          return false;
      }

      /*
       *  Retorna true el elemento en la posicion s del heap deberia intercambiarse con el
       *  elemento en la posicion f del heap. Se supone que heap[s] es hijo (izquierdo o 
       *  derecho) de heap[f], aunque el m�todo no valida ese supuesto. La comprobacion se
       *  realiza de acuerdo al tipo de heap: si es un heap ascendente, se retornara true si
       *  heap[s] < heap[f], pero si el heap es descendente se retornara true si heap[s] > heap[f].
       */
      private boolean valid_change(int s, int f)
      {          
          // comparar el hijo (heap[s]) con el padre (heap[f])...
          int r = heap[s].getData().compareTo(heap[f].getData());
          
          // ...si el menor debiera estar arriba, pero esta abajo, retorne true...          
          if(ascendent && r < 0) { return true; }
                
          // ...si el mayor debiera estar arriba, pero esta abajo, retorne true...          
          if( ! ascendent && r > 0) { return true; }
          
          // en cualquier otro caso, esta todo en orden... y no hay que hacer cambios... 
          return false;
      }

      private class Slot <T extends Comparable>
      {
           private T data;
           
           public Slot(T d)
           {
               data = d;
           }
           
           public T getData()
           {
               return data;
           }
           
           public void setData(T d)
           {
               if(d != null) { data = d; }
           }
           
           
           @Override
           public String toString()
           {
               return data.toString();
           }
      }
}
