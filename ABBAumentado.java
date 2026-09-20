public class ABBAumentado <K extends Comparable<? super K>, V> {

    public static class Nodo <K, V> {
        K clave;
        V valor;
        Nodo<K, V> izq, der;
        int tamano;

        public Nodo ( K clave, V valor ) {
            this.clave = clave;
            this.valor = valor;
            izq = der = null;
            tamano = 1;
        } // <--> end Nodo constructor


    } // <-> end Nodo class


    private Nodo<K, V> raiz;
    private long visitas;

    public ABBAumentado () {
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
        this.visitas += 1;
        short tamaño_extra;

        // clave repetida
        if ( clave.compareTo(nodo.clave) == 0 ) {
            nodo.valor = valor;  // cambia el valor al nuevo
            return 0;   // retorna 0 para no modificar los tamaños de sus antecesores

        // clave < nodo.clave (<-)
        } else if ( clave.compareTo( nodo.clave ) < 0 ) {

            if ( nodo.izq == null ) {   // insercion directa
                nodo.izq = new Nodo<K, V>( clave, valor );
                return 1;   // retorna 1 para modificar los tamaños de sus ancestros +1
            }

            tamaño_extra = agregar_recursivo( clave, valor, nodo.izq );    // recorrer a la izquierda (<-)
            nodo.tamano += tamaño_extra;
            return tamaño_extra;

        // clave > nodo.clave (->)
        } else if ( clave.compareTo( nodo.clave ) > 0 ) {

            if ( nodo.der == null ) { // insercion directa
                nodo.der = new Nodo<K, V>( clave, valor );
                return 1;   // retorna 1 para modificar los tamaños de sus ancestros +1
            }

            tamaño_extra = agregar_recursivo( clave, valor, nodo.der );    // recocrrer a la derecha (->)
            nodo.tamano += tamaño_extra;
            return tamaño_extra;
        }


        return -1;
    } // <-> end agregar recursivo method
    // agregar <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    // Eliminar >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public V eliminar ( K clave ) throws ClaveInexistenteException {
        if ( clave == null ) { throw new ClaveNulaException(); }

        this.visitas += 1;
        if ( raiz.clave.compareTo( clave ) == 0 ) {
            V data = raiz.valor;
            if ( raiz.izq != null ) {
                raiz = replace_deleted(raiz, raiz);
            }

            raiz = raiz.der;
            return data;
        }

        this.visitas -= 1; // en eliminar_recursivo se volvera a agregar la vista a la raiz
        return eliminar_recursivo(clave, raiz, raiz, false);
    }



    private V eliminar_recursivo ( K clave, Nodo<K, V> padre, Nodo<K, V> hijo, boolean der ) throws ClaveInexistenteException { // der (false -> izq)

        if ( hijo == null ) { throw new ClaveInexistenteException("clave inexistente"); }

        // 1. busqueda del nodo a eliminar
        if ( hijo.clave.compareTo(clave) == 0 ) { // se encuentra el nodo

            // comprobar si posee rama izquierda
            if ( hijo.izq != null ) {
                Nodo<K, V> replace = replace_deleted(hijo.izq, hijo.izq);

                if ( ! ( replace.clave.compareTo( hijo.izq.clave ) == 0 ) ) {   // si solo queda un nodo en la rama izq

                    if ( der ) {
                        padre.der = hijo.izq;
                    } else { padre.izq = hijo.izq; }

                } else {
                    replace.izq = null;
                }

                replace.der = hijo.der;

                if ( der ) {
                    padre.der = replace;
                } else {
                    padre.izq = replace;
                }

                return hijo.valor;

            // si no posee izquierda entonces se reemplaza directo por hijo.der
            } else {
                if ( der ) {
                    padre.der = hijo.der;
                } else {
                    padre.izq = hijo.der;
                }
            }
        }
        
        // 2. busqueda
        if ( hijo.clave.compareTo(clave) < 0 ) { // menor ->
            return eliminar_recursivo(clave, hijo, hijo.izq, true);

        } else {
            return eliminar_recursivo(clave, hijo, hijo.der, false);
        }

    }



        // esta funcion la utiliza eliminar_recursivo
    private Nodo<K, V> replace_deleted ( Nodo<K, V> padre, Nodo<K, V> hijo ) {
        if ( hijo.der == null ) { // el mas grande no tiene subarbol derecho

            if ( hijo == padre ) {
                return hijo;
            }

            padre.der = hijo.izq;
            return hijo;    // el retorno es el nodo que reemplazara al eliminado
        }

        return replace_deleted (hijo, hijo.der);  // recorre en busca del mas grande
    }

    // Eliminar <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


} // <> end ABBAumentado class
