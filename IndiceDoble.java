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

public class IndiceDoble <K extends Comparable<? super K>, V> {

    private ABBAumentado<K, V> arbol;

    private TablaEncadenada<K, ABBAumentado.Nodo<K, V>> tabla;


    public IndiceDoble () {
        this.arbol = new ABBAumentado<K, V>();
        this.tabla = new TablaEncadenada<K, ABBAumentado.Nodo<K, V>>();
    } // <-> end IndiceDoble constructor



    public void agregar ( K clave, V valor ) {
        if ( clave == null ) { throw new ClaveNulaException(); }

        arbol.agregar( clave, valor );  // si la clave ya estaba, el arbol solo cambia el valor del nodo

        // se busca el nodo que quedo en el arbol para guardar su referencia en la tabla.
        ABBAumentado.Nodo<K, V> nodo = arbol.buscarNodo( clave );

        // si la clave ya estaba, la tabla actualiza el dato con la misma referencia (no duplica)
        tabla.insertar( clave, nodo );
    } // <-> end agregar method




    // resuelve solo por la tabla: NO incrementa arbol.visitas()
    public V obtener ( K clave ) throws ClaveInexistenteException {
        if ( clave == null ) { throw new ClaveNulaException(); }

        ABBAumentado.Nodo<K, V> nodo = tabla.obtener( clave );  // recorre la cubeta, no el arbol
        if ( nodo == null ) { throw new ClaveInexistenteException("clave inexistente"); }

        return nodo.valor;      // se sigue la referencia al nodo del arbol
    } // <-> end obtener method



    public boolean contiene ( K clave ) {
        if ( clave == null ) { throw new ClaveNulaException(); }

        return tabla.obtener( clave ) != null;
    } // <-> end contiene method



    public V eliminar ( K clave ) throws ClaveInexistenteException {
        if ( clave == null ) { throw new ClaveNulaException(); }

        // primero el arbol: si la clave no esta lanza ClaveInexistenteException y la tabla no se toca
        V valor = arbol.eliminar( clave );
        tabla.eliminar( clave );

        return valor;
    } // <-> end eliminar method



    public K kEsimo ( int k ) { return arbol.kEsimo( k ); } // <-> end kEsimo method


    public int consultarRango ( K a, K b ) { return arbol.consultarRango( a, b ); }


    public int size () { return arbol.size(); } // <-> end size method


    // para la clase de prueba: poder leer arbol.visitas() y tabla.sondas()
    public ABBAumentado<K, V> arbol () { return arbol; }


    public TablaEncadenada<K, ABBAumentado.Nodo<K, V>> tabla () { return tabla; }
} // <> end IndiceDoble class
