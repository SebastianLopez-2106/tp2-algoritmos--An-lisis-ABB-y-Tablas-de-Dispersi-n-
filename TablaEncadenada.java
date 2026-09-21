/**
 * Grupo: g_tq24
 * Integrantes:
 * - Lopez Guerreros, Sebastian Alejandro, 6.153.672, TQ
 * - Oviedo Fernandez, Blas Nazario, 7.037.075, TQ
 *
 * Declaración de Honor:
 * • Nosotros Sebastian Lopez y Blas Oviedo:
 * • No hemos discutido el código fuente de nuestra tarea con ningún otro
 *   grupo, solo con el Profesor o el AER.
 * • No hemos usado código obtenido de otro estudiante o de cualquier otra
 *   fuente no autorizada, modificada o no modificada.
 * • Cualquier código o documentación utilizada en nuestro programa
 *   obtenido de fuentes, tales como libros o notas de curso, han sido claramente
 *   indicada en nuestra tarea
 */

public class TablaEncadenada <K, V> {

    // nodo de la lista enlazada de cada cubeta (lista propia)
    private static class NodoLista <K, V> {
        K clave;
        V dato;
        NodoLista<K, V> sig;

        public NodoLista ( K clave, V dato, NodoLista<K, V> sig ) {
            this.clave = clave;
            this.dato = dato;
            this.sig = sig;
        } // <--> end NodoLista constructor


    } // <-> end NodoLista class


    public static final int TAM_INICIAL = 11;   // cantidad inicial de cubetas (es primo)

    private int m, n;                   // m = cantidad de cubetas, n = cantidad de claves guardadas
    private NodoLista<K, V> [] index;   // cubetas: cada posicion apunta al primer nodo de su lista
    private double alpha_max;           // factor de carga maximo, si n/m lo supera se hace rehash
    private long sondas;                // nodos de cadena visitados desde la creacion (o desde el ultimo reinicio)


    public TablaEncadenada () {
        this( TAM_INICIAL, 1.0 );
    } // <-> end TablaEncadenada constructor



    // para el experimento con alfa fijo: alfaMax = Double.POSITIVE_INFINITY -> nunca hay rehash
    @SuppressWarnings("unchecked")  // para quitar la advertencia
    public TablaEncadenada ( int m, double alfaMax ) {
        if ( m <= 0 ) { throw new IllegalArgumentException("m debe ser positivo"); }

        this.m = m;
        this.n = 0;
        this.alpha_max = alfaMax;
        this.sondas = 0;
        this.index = (NodoLista<K, V> []) new NodoLista[m];
    } // <-> end TablaEncadenada constructor (con m y alfaMax)



    // hash funtion
    public int h ( K key ) { return ( key.hashCode() & 0x7fffffff ) % m; }



    // insertar >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void insertar ( K key, V dato ) {
        if ( key == null ) { throw new ClaveNulaException(); }

        int pos = h( key );
        NodoLista<K, V> actual = index[pos];

        // se recorre la cubeta por si la clave ya estaba
        while ( actual != null ) {
            this.sondas++;

            if ( actual.clave.equals( key ) ) {
                actual.dato = dato;     // clave repetida: solo se actualiza el dato
                return;
            }

            actual = actual.sig;
        }

        // clave nueva: se inserta al frente de la cubeta
        index[pos] = new NodoLista<K, V>( key, dato, index[pos] );
        n++;

        // si se supera el factor de carga maximo se duplica m y se reubican las claves
        if ( factorCarga() > alpha_max ) { rehash(); }
    } // <-> end insertar method



    // para utilizarlo en insertar
    @SuppressWarnings("unchecked")  // para quitar la advertencia
    private void rehash () {
        NodoLista<K, V> [] viejo = index;
        int m_viejo = m;

        m = 2 * m;      // h() ya usa el m nuevo
        index = (NodoLista<K, V> []) new NodoLista[m];

        // se reenlazan los mismos nodos de lista en las cubetas nuevas (no se crean nodos nuevos)
        for ( int i = 0; i < m_viejo; i++ ) {
            NodoLista<K, V> actual = viejo[i];

            while ( actual != null ) {
                NodoLista<K, V> siguiente = actual.sig;     // se guarda antes de cambiar actual.sig

                int pos = h( actual.clave );
                actual.sig = index[pos];    // se pone al frente de la cubeta nueva
                index[pos] = actual;

                actual = siguiente;
            }
        }
    } // <-> end rehash method (priv)
    // insertar ------------------------------------------------------------------------------------------+



    // retorna el dato o null si la clave no esta
    public V obtener ( K key ) {
        if ( key == null ) { throw new ClaveNulaException(); }

        NodoLista<K, V> actual = index[ h( key ) ];

        while ( actual != null ) {
            this.sondas++;

            if ( actual.clave.equals( key ) ) { return actual.dato; }

            actual = actual.sig;
        }

        return null;
    } // <-> end obtener method



    // saca la clave de su cubeta y retorna el dato, o null si la clave no esta
    public V eliminar ( K key ) {
        if ( key == null ) { throw new ClaveNulaException(); }

        int pos = h( key );
        NodoLista<K, V> anterior = null;
        NodoLista<K, V> actual = index[pos];

        while ( actual != null ) {
            this.sondas++;

            if ( actual.clave.equals( key ) ) {

                if ( anterior == null ) { index[pos] = actual.sig; }    // era el primero de la cubeta
                else { anterior.sig = actual.sig; }

                n--;
                return actual.dato;
            }

            anterior = actual;
            actual = actual.sig;
        }

        return null;
    } // <-> end eliminar method



    public int capacidad () { return m; } // <-> end capacidad method

    public int size () { return n; } // <-> end size method

    public double factorCarga () { return (double) n / m; } // <-> end factorCarga method



    public long sondas () { return this.sondas; } // <-> end sondas method

    public void reiniciarSondas () { this.sondas = 0; } // <-> end reiniciarSondas method



    // una linea por cubeta, para la traza. Ejemplo: [ 6] 61 -> 50
    public String dump () {
        String resultado = "";

        for ( int i = 0; i < m; i++ ) {
            if ( i < 10 ) { resultado += "[ " + i + "]"; }
            else { resultado += "[" + i + "]"; }

            NodoLista<K, V> actual = index[i];
            if ( actual == null ) { resultado += " -"; }   // cubeta vacia

            while ( actual != null ) {
                resultado += " " + actual.clave;
                if ( actual.sig != null ) { resultado += " ->"; }

                actual = actual.sig;
            }

            resultado += "\n";
        }

        return resultado;
    } // <-> end dump method


} // <> end TablaEncadenada class
