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


import java.util.Iterator;

public class ABBAumentado <K extends Comparable<? super K>, V> implements Iterable<K> {

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
        } 

        // Getters
        public K clave() { return clave; }
        public V valor() { return valor; }
        public int tamano() { return tamano; }
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
    // agregar -------------------------------------------------------------------------------------------+


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

    } // <-> end eliminar_recursivo method (priv)



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

    } // <-> end extraer_sucesor method (priv)

    // Eliminar -------------------------------------------------------------------------------------------+



    public V obtener ( K clave ) throws ClaveInexistenteException {
        if ( clave == null ) { throw new ClaveNulaException(); }

        Nodo<K, V> nodo = raiz;
        while ( nodo != null ) {
            this.visitas++;
            if ( nodo.clave.compareTo( clave ) == 0 ) { return nodo.valor; }

            if ( nodo.clave.compareTo( clave ) < 0 ) { nodo = nodo.der; }
            else { nodo = nodo.izq; }
        }

        throw new ClaveInexistenteException("clave inexistente");
    } // <-> end obtener method



    public int cuantosMenores ( K clave) {
        if ( clave == null ) { throw new ClaveNulaException(); }

        // CountSmallerA se encuentra en ConsultarRango
        return CountSmallerA( clave, raiz, 0 );
    } // <-> end cuantosMenores method



    // ConsultarRango >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public int consultarRango ( K a, K b) {
        if ( a == null || b == null ) { throw new ClaveNulaException(); }
        if ( a.compareTo( b ) > 0 ) { throw new RangoInvalidoException(" a < b (Rango invalido)"); }

        if ( raiz == null ) { return 0; }
        return raiz.tamano - CountSmallerA( a, raiz, 0 ) - CountSmallerB( b, raiz, 0 );
    } // <-> end consultarRango method



    // retorna la cantidad de elementos que son menores que 'a' (fuera de rango)
    private int CountSmallerA ( K A, Nodo<K, V> nodo , int cantidad ) {
        // casos bases
            // 1. se encuentra una hoja nula (subarbol vacio)
        if ( nodo == null ) { return cantidad; }

        this.visitas++;

            // 2. se encuentra 'a'
        if ( nodo.clave.compareTo( A ) == 0 ) {
            if ( nodo.izq != null ) { cantidad += nodo.izq.tamano; }    // se suma el tamaño del subarbol izquierdo
            return cantidad;
        }


        // recorrido
            // 1. el nodo es mayor a 'a': recorrido izquierdo (no suma nada a cantidad)
        if ( nodo.clave.compareTo( A ) > 0 ) {
            return CountSmallerA( A, nodo.izq, cantidad ); // (<-)
        }

            // 2. el nodo es menor que 'a': recorrido derecho (suma el nodo y el tamaño de
            //     su subarbol izquierdo a cantidad)
        if ( nodo.clave.compareTo( A ) < 0 ) {
            int cant_izq = 0;
            if ( nodo.izq != null ) { cant_izq = nodo.izq.tamano; } // sumo el subarbol izquierdo a cantidad
            return CountSmallerA( A, nodo.der, cantidad ) + 1 + cant_izq; // (->)
        }
        return -1;
    } // <-> end CountSmallerA method (priv)



    // retorna la cantidad de elementos que son mayores que 'b' (fuera de rango)
    private int CountSmallerB ( K B, Nodo<K, V> nodo , int cantidad ) {
        // casos bases
            // 1. se encuentra una hoja nula (subarbol vacio)
        if ( nodo == null ) { return cantidad; }

        this.visitas++;

            // 2. se encuentra 'a'
        if ( nodo.clave.compareTo( B ) == 0 ) {
            if ( nodo.der != null ) { cantidad += nodo.der.tamano; }    // se suma el tamaño del subarbol derecho
            return cantidad;
        }


        // recorrido
            // 1. el nodo es mayor a 'b': recorrido izquierdo (suma el nodo y el tamaño de
            //     su subarbol derecho)
        if ( nodo.clave.compareTo( B ) > 0 ) {
            int cant_der = 0;
            if ( nodo.der != null ) { cant_der = nodo.der.tamano; } // sumo el subarbol derecho a cantidad
            return CountSmallerB( B, nodo.izq, cantidad ) + 1 + cant_der; // (<-)
        }

            // 2. el nodo es menor que 'b': recorrido derecho (no le suma nada a cantidad)
        if ( nodo.clave.compareTo( B ) < 0 ) {
            return CountSmallerB( B, nodo.der, cantidad ); // (->)
        }
        return -1;
    } // <-> end CountSmallerB method (priv)
    // ConsultarRango ------------------------------------------------------------------------------------+



    public K sucesor ( K clave ) throws ClaveInexistenteException {
        if ( clave == null ) { throw new ClaveNulaException(); }

        Nodo<K, V> nodo = raiz;
        K posible_sucesor = null; // MODIFICADO: guarda el ultimo ancestro por el que bajamos a la izquierda

        while ( nodo != null ) {
            this.visitas++; // MODIFICADO: incrementa el contador de visitas al mirar un nodo

            if ( nodo.clave.compareTo( clave ) == 0) {

                if ( nodo.der != null ) { // MODIFICADO: si tiene rama derecha, buscamos el minimo ahi
                    Nodo <K, V> nodo2 = nodo.der;
                    while ( nodo2 != null ) {
                        this.visitas++; // MODIFICADO: incrementa visitas en el sub-bucle
                        if ( nodo2.izq == null ) { return nodo2.clave; }

                        nodo2 = nodo2.izq;
                    }
                }

                return posible_sucesor; // MODIFICADO: si no hay rama derecha, retorna el ancestro guardado (o null si es el maximo)
            }

            if ( nodo.clave.compareTo( clave ) < 0 ) {
                nodo = nodo.der;
            }
            else if ( nodo.clave.compareTo( clave ) > 0 ) {
                posible_sucesor = nodo.clave; // MODIFICADO: guardamos este nodo como candidato antes de bajar a la izquierda
                nodo = nodo.izq;
            }
        }

        throw new ClaveInexistenteException("clave inexistente");
    } // <-> end sucesor method



    public K predecesor ( K clave ) throws ClaveInexistenteException {
        if ( clave == null ) { throw new ClaveNulaException(); }

        Nodo<K, V> nodo = raiz;
        K posible_predecesor = null; // MODIFICADO: guarda el ultimo ancestro por el que bajamos a la derecha

        while ( nodo != null ) {
            this.visitas++; // MODIFICADO: incrementa el contador de visitas al mirar un nodo

            if ( nodo.clave.compareTo( clave ) == 0) {

                if ( nodo.izq != null ) { // MODIFICADO: si tiene rama izquierda, buscamos el maximo ahi
                    Nodo <K, V> nodo2 = nodo.izq;
                    while ( nodo2 != null ) {
                        this.visitas++; // MODIFICADO: incrementa visitas en el sub-bucle
                        if ( nodo2.der == null ) { return nodo2.clave; }

                        nodo2 = nodo2.der;
                    }
                }

                return posible_predecesor; // MODIFICADO: si no hay rama izquierda, retorna el ancestro guardado (o null si es el minimo)
            }

            if ( nodo.clave.compareTo( clave ) < 0 ) {
                posible_predecesor = nodo.clave; // MODIFICADO: guardamos este nodo como candidato antes de bajar a la derecha
                nodo = nodo.der;
            }
            else if ( nodo.clave.compareTo( clave ) > 0 ) {
                nodo = nodo.izq;
            }
        }

        throw new ClaveInexistenteException("clave inexistente");
    } // <-> end predecesor method



    // tamanosConsistentes >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public boolean tamanosConsistentes () { return verificar_recursivo(raiz) >= 0; } // <-> end tamanosConsistentes method



    private int verificar_recursivo ( Nodo<K, V> nodo ) {
        if ( nodo == null ) { return 0; }

        int nodo_izq = verificar_recursivo ( nodo.izq );
        int nodo_der = verificar_recursivo ( nodo.der );

        if ( nodo_izq < 0 || nodo_der < 0 ) { return -1; }

        if ( nodo.tamano == 1 + nodo_izq + nodo_der ) { return nodo.tamano; }
        else { return -1; }
    } // <-> end verificar_recursivo method (priv)
    // tamanosConsistentes ----------------------------------------------------------------------+



    // buscarNodo >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    Nodo<K, V> buscarNodo ( K clave ) {
        if ( clave == null ) { throw new ClaveNulaException(); }

        Nodo<K, V> nodo = raiz;
        while ( nodo != null ) {
            this.visitas++;
            if ( nodo.clave.compareTo( clave ) == 0 ) { return nodo; }

            if ( nodo.clave.compareTo( clave ) < 0 ) { nodo = nodo.der; }
            else { nodo = nodo.izq; }
        }

        return null;
    } // <-> end buscarNodo method
    // buscarNodo ----------------------------------------------------------------------------------------+



    public boolean contiene ( K clave ) {
        if ( clave == null ) { throw new ClaveNulaException(); }

        return buscarNodo( clave ) != null;
    } // <-> end contiene method



    // kEsimo >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // k-esimo menor (1-based), baja por un solo camino mirando el tamano del hijo izquierdo
    public K kEsimo ( int k ) {
        if ( k < 1 || k > size() ) { throw new IndiceFueraDeRangoException("k fuera de rango"); }

        Nodo<K, V> nodo = raiz;
        while ( nodo != null ) {
            this.visitas++;

            int tam_izq = 0;
            if ( nodo.izq != null ) { tam_izq = nodo.izq.tamano; }

            if ( k == tam_izq + 1 ) { return nodo.clave; }  // este nodo es la respuesta

            if ( k <= tam_izq ) { nodo = nodo.izq; }    // (<-) con el mismo k
            else {
                k = k - tam_izq - 1;    // (->) se descartan el lado izquierdo y este nodo
                nodo = nodo.der;
            }
        }

        return null;    // no deberia llegar si los tamanos son consistentes
    } // <-> end kEsimo method
    // kEsimo --------------------------------------------------------------------------------------------+



    public int size () {
        if ( raiz == null ) { return 0; }
        return raiz.tamano;
    } // <-> end size method



    public long visitas () { return this.visitas; } // <-> end visitas method

    public void reiniciarVisitas () { this.visitas = 0; } // <-> end reiniciarVisitas method



    // toString >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // inorden con tamanos
    public String toString () {
        return toString_recursivo( raiz ).trim();
    } // <-> end toString method



    private String toString_recursivo ( Nodo<K, V> nodo ) {
        if ( nodo == null ) { return ""; }

        return toString_recursivo( nodo.izq ) + nodo.clave + "(" + nodo.tamano + ") " + toString_recursivo( nodo.der );
    } // <-> end toString_recursivo method (priv)
    // toString ------------------------------------------------------------------------------------------+



    // altura >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public int altura() {
        return altura_recursiva(raiz);
    }

    private int altura_recursiva(Nodo<K, V> nodo) {
        if (nodo == null) { return -1; }
        int alt_izq = altura_recursiva(nodo.izq);
        int alt_der = altura_recursiva(nodo.der);
        return 1 + Math.max(alt_izq, alt_der);
    }
    // altura -----------------------------------------------------------------------------------------+



    // consultarRangoIngenuo >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public int consultarRangoIngenuo(K a, K b) {
        if (a == null || b == null) { throw new ClaveNulaException(); }
        if (a.compareTo(b) > 0) { throw new RangoInvalidoException("a > b"); }
        return inorden_ingenuo(raiz, a, b);
    }

    private int inorden_ingenuo(Nodo<K, V> nodo, K a, K b) {
        if (nodo == null) { return 0; }
        this.visitas++;
        int count = 0;
        count += inorden_ingenuo(nodo.izq, a, b);
        if (nodo.clave.compareTo(a) >= 0 && nodo.clave.compareTo(b) <= 0) {
            count++;
        }
        count += inorden_ingenuo(nodo.der, a, b);
        return count;
    }
    // consultarRangoIngenuo --------------------------------------------------------------------------+




    public int rango(K clave) throws ClaveInexistenteException {
        if (clave == null) { throw new ClaveNulaException(); }
        if (!contiene(clave)) { throw new ClaveInexistenteException("clave inexistente"); }
        return cuantosMenores(clave) + 1;
    }



    // iterator >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Override
    public Iterator<K> iterator() {
        @SuppressWarnings("unchecked")
        K[] elementos = (K[]) new Comparable[size()];
        llenar_inorden(raiz, elementos, new int[]{0});

        return new Iterator<K>() {
            private int i = 0;

            @Override
            public boolean hasNext() { return i < elementos.length; }

            @Override
            public K next() {
                if (!hasNext()) throw new RuntimeException("No hay elementos");
                return elementos[i++];
            }
        };
    } // <-> end Iterator method

    private void llenar_inorden(Nodo<K, V> nodo, K[] arr, int[] idx) {
        if (nodo == null) return;
        llenar_inorden(nodo.izq, arr, idx);
        arr[idx[0]++] = nodo.clave;
        llenar_inorden(nodo.der, arr, idx);
    } // <-> end llenar_inorder method
    // iterator ---------------------------------------------------------------------------------------+




} // <> end ABBAumentado class