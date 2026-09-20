public class ABBAumentado <K extends Comparable<? super K>, V> {

    public static class Nodo <K, V> {
        K clave;
        V valor;
        Nodo<K, V> izq, der;
        long tamaño;

        public Nodo ( K clave, V valor ) {
            this.clave = clave;
            this.valor = valor;
            izq = der = null;
            tamaño = 1;
        } // <--> end Nodo constructor


    } // <-> end Nodo class


    private Nodo<K, V> raiz;
    @SuppressWarnings("unused")
    private long visitas;

    public ABBAumentado ( K key, V value ) {
        this.raiz = null;
        this.visitas = 0;
    } // <-> end ABBAumentado constructor



    // agregar >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void agregar ( K clave, V valor ) {
        if ( clave == null ) { throw new ClaveNulaException(); } // exception if clave == null

        if ( raiz == null ) {
            raiz = new Nodo<K,V>( clave, valor );

        } else {
            agregar_recursivo(clave, valor, raiz);
        }

    } // <-> end agregar method


        // para utilizarlo en agregar
    private short agregar_recursivo ( K clave, V valor, Nodo<K, V> nodo ) {
        short tamaño_extra;

        // clave repetida
        if ( clave.compareTo(nodo.clave) == 0 ) {
            nodo.valor = valor;  // cambia el valor al nuevo
            return 0;   // retorna 0 para no modificar los tamaños de sus antecesores

        // clave < nodo.clave (<-)
        } else if ( clave.compareTo( nodo.clave ) < 0 ) {

            if ( nodo.izq == null ) {   // insercion directa
                nodo.izq = new Nodo<K, V>( clave, valor );
                nodo.tamaño += 1;
                return 1;   // retorna 1 para modificar los tamaños de sus ancestros +1
            }

            tamaño_extra = agregar_recursivo( clave, valor, nodo.izq );    // recorrer a la izquierda (<-)
            nodo.tamaño += tamaño_extra;
            return tamaño_extra;

        // clave > nodo.clave (->)
        } else if ( clave.compareTo( nodo.clave ) > 0 ) {

            if ( nodo.der == null ) { // insercion directa
                nodo.der = new Nodo<K, V>( clave, valor );
                nodo.tamaño += 1;
                return 1;   // retorna 1 para modificar los tamaños de sus ancestros +1
            }

            tamaño_extra = agregar_recursivo( clave, valor, nodo.der );    // recocrrer a la derecha (->)
            nodo.tamaño += tamaño_extra;
            return tamaño_extra;
        }


        return -1;
    } // <-> end agregar recursivo method
    // agregar <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




} // <> end ABBAumentado class
