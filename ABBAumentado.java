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
                nodo.tamano += 1;
                return 1;   // retorna 1 para modificar los tamaños de sus ancestros +1
            }

            tamaño_extra = agregar_recursivo( clave, valor, nodo.izq );    // recorrer a la izquierda (<-)
            nodo.tamano += tamaño_extra;
            return tamaño_extra;

        // clave > nodo.clave (->)
        } else if ( clave.compareTo( nodo.clave ) > 0 ) {

            if ( nodo.der == null ) { // insercion directa
                nodo.der = new Nodo<K, V>( clave, valor );
                nodo.tamano += 1;
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
        if ( raiz == null ) { throw new ClaveInexistenteException("clave inexistente"); }
        if ( clave == null ) { throw new ClaveNulaException(); }

        this.visitas += 1;
        if ( raiz.clave.compareTo( clave ) == 0 ) {

            V data = raiz.valor;

            if ( raiz.izq != null && raiz.der == null ) {
                raiz = raiz.izq; // la raiz pasa a ser su hijo izquierdo

            } else if ( raiz.izq == null && raiz.der != null ) {
                raiz = raiz.der; // la raiz pasa a ser su hijo derecho

            } else if ( raiz.izq != null && raiz.der != null ) { // caso raiz con dos hijos

                Nodo<K, V> arbol_izquierdo = raiz.izq;
                Nodo<K, V> arbol_derecho = raiz.der;
                raiz = extraer_sucesor( raiz.der, raiz.der );

                raiz.izq = arbol_izquierdo; // vinculamos la rama izquierda al sucesor para no perderla

                if (raiz != arbol_derecho) { // previene la autorreferencia
                    raiz.der = arbol_derecho;
                }

            } else { // si la raiz es una hoja
                raiz = null;
            }

            // recalcular el tamaño de la raiz
            if ( raiz != null ) {
                raiz.tamano = 1;
                if ( raiz.izq != null ) { raiz.tamano += raiz.izq.tamano; }
                if ( raiz.der != null ) { raiz.tamano += raiz.der.tamano; }
            }

            return data;
        }

        this.visitas -= 1; // en eliminar_recursivo se volvera a agregar la vista a la raiz
        return eliminar_recursivo(clave, raiz, raiz, false);
    } // <-> end elminimar method



    private V eliminar_recursivo ( K clave, Nodo<K, V> padre, Nodo<K, V> hijo, boolean der ) throws ClaveInexistenteException { // der (false -> izq)

        this.visitas++;

        if ( hijo == null ) { throw new ClaveInexistenteException("clave inexistente"); }

        // 1. busqueda del nodo a eliminar
        if ( hijo.clave.compareTo(clave) == 0 ) { // se encuentra el nodo

            // comprobar si posee rama izquierda
            if ( hijo.izq != null && hijo.der == null ) {
                if ( der ) { padre.der = hijo.izq; }
                else { padre.izq = hijo.izq; }
            }

            else if ( hijo.izq == null && hijo.der != null ) {
                if ( der ) { padre.der = hijo.der; }
                else { padre.izq = hijo.der; }
            }

            else if ( hijo.izq != null && hijo.der != null ) {
                Nodo<K, V> replace = extraer_sucesor(hijo.der, hijo.der);

                // vincular replace al padre del eliminado
                if ( der ) {
                    padre.der = replace;
                } else { padre.izq = replace; }

                // salvar el subarbol derecho e izquierdo del eliminado
                replace.izq = hijo.izq;
                if (replace != hijo.der) { // previene la autoreferencia
                    replace.der = hijo.der;
                }

                // recalcular tamaño de replace
                replace.tamano = 1;
                if ( replace.izq != null ) { replace.tamano += replace.izq.tamano; }
                if ( replace.der != null ) { replace.tamano += replace.der.tamano; }

            }

            else { // ambas ramas son nulas
                if ( der ) { padre.der = null; }
                else { padre.izq = null; }
            }

            return hijo.valor;
        }

        // 2. busqueda
        if ( hijo.clave.compareTo(clave) < 0 ) { // menor (->)
            V temp_value = eliminar_recursivo(clave, hijo, hijo.der, true);
            hijo.tamano -= 1;
            return temp_value;

        } else { // mayor (<-)
            V temp_value = eliminar_recursivo(clave, hijo, hijo.izq, false);
            hijo.tamano -= 1;
            return temp_value;
        }

    } // <-> end eliminar_recursivo method


 
        // esta funcion la utiliza eliminar_recursivo
    private Nodo<K, V> extraer_sucesor ( Nodo<K, V> padre, Nodo<K, V> hijo ) {
        this.visitas++;

        if ( hijo.izq == null ) { // el mas pequeño no tiene subarbol izquierdo

            if ( hijo == padre ) {
                return hijo;
            }

            padre.izq = hijo.der; // se vincula para salvar el hijo derecho antes de ser reemplazado
            return hijo;    // el retorno es el nodo que reemplazara al eliminado
        }

        hijo.tamano -= 1;
        return extraer_sucesor (hijo, hijo.izq);  // baja por la izquierda buscando el minimo

    } // <-> end extraer_sucesor method

    // Eliminar <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


} // <> end ABBAumentado class