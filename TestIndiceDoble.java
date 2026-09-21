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

import java.util.Random;

public class TestIndiceDoble {

    static int errores = 0;     // cantidad de verificaciones que fallaron


    // imprime [OK] o [ERROR] segun la condicion
    static void verificar ( String descripcion, boolean condicion ) {
        if ( condicion ) { System.out.println("  [OK]    " + descripcion); }
        else {
            System.out.println("  [ERROR] " + descripcion);
            errores++;
        }
    } // <-> end verificar method



    // dump() de la tabla, toString() del arbol, ambos tamanos y ambos contadores
    static void mostrar_estado ( IndiceDoble<Integer, String> indice ) {
        System.out.print( indice.tabla().dump() );
        System.out.println("  arbol.toString(): " + indice.arbol().toString());
        System.out.println("  size arbol = " + indice.arbol().size() + " | size tabla = " + indice.tabla().size());
        System.out.println("  arbol.visitas() = " + indice.arbol().visitas() + " | tabla.sondas() = " + indice.tabla().sondas());
    } // <-> end mostrar_estado method



    // permutacion de 1..n con Random(2026) y barajado de Fisher-Yates (el codigo de referencia del enunciado)
    static int[] permutacion ( int n ) {
        Random rng = new Random(2026);
        int[] a = new int[n];

        for ( int i = 0; i < n; i++ ) { a[i] = i + 1; }

        for ( int i = n - 1; i > 0; i-- ) {
            int j = rng.nextInt( i + 1 );
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return a;
    } // <-> end permutacion method



    // redondea a 2 decimales para imprimir
    static double redondear ( double x ) { return Math.round( x * 100 ) / 100.0; } // <-> end redondear method



    public static void main ( String[] args ) throws ClaveInexistenteException {

        IndiceDoble<Integer, String> indice = new IndiceDoble<Integer, String>();


        // Fase A >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        System.out.println("=== Fase A: agregar 50, 30, 70, 20, 40, 60, 80, 35, 65 ===");

        indice.arbol().reiniciarVisitas();
        indice.tabla().reiniciarSondas();

        int[] claves_traza = { 50, 30, 70, 20, 40, 60, 80, 35, 65 };
        for ( int i = 0; i < claves_traza.length; i++ ) {
            indice.agregar( claves_traza[i], "P" + claves_traza[i] );
        }

        mostrar_estado( indice );

        String dump_a = "[ 0] -\n[ 1] -\n[ 2] 35\n[ 3] 80\n[ 4] 70\n[ 5] 60\n[ 6] 50\n[ 7] 40\n[ 8] 30\n[ 9] 20\n[10] 65\n";
        verificar( "cubetas ocupadas: 2->35 3->80 4->70 5->60 6->50 7->40 8->30 9->20 10->65", indice.tabla().dump().equals( dump_a ) );
        verificar( "arbol: 20(1) 30(4) 35(1) 40(2) 50(9) 60(2) 65(1) 70(4) 80(1)",
                   indice.arbol().toString().equals( "20(1) 30(4) 35(1) 40(2) 50(9) 60(2) 65(1) 70(4) 80(1)" ) );
        verificar( "size arbol = size tabla = 9", indice.arbol().size() == 9 && indice.tabla().size() == 9 );
        verificar( "m = 11 (todavia no hubo rehash)", indice.tabla().capacidad() == 11 );
        // Fase A -------------------------------------------------------------------------------------------+


        // Fase B >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        System.out.println();
        System.out.println("=== Fase B: agregar 61 y 41 (colisiones en cubeta 6 y 8) ===");

        indice.arbol().reiniciarVisitas();
        indice.tabla().reiniciarSondas();

        indice.agregar( 61, "P61" );
        indice.agregar( 41, "P41" );

        mostrar_estado( indice );

        verificar( "61 y 50 en la cubeta 6, 41 y 30 en la cubeta 8",
                   indice.tabla().h( 61 ) == 6 && indice.tabla().h( 50 ) == 6 && indice.tabla().h( 41 ) == 8 && indice.tabla().h( 30 ) == 8 );
        verificar( "dump: [ 6] 61 -> 50 y [ 8] 41 -> 30, el resto no cambia",
                   indice.tabla().dump().equals( dump_a.replace( "[ 6] 50", "[ 6] 61 -> 50" ).replace( "[ 8] 30", "[ 8] 41 -> 30" ) ) );
        verificar( "size arbol = size tabla = 11", indice.arbol().size() == 11 && indice.tabla().size() == 11 );
        verificar( "m = 11 (alfa = 1 no supera alfaMax = 1, no hay rehash)", indice.tabla().capacidad() == 11 );
        // Fase B -------------------------------------------------------------------------------------------+


        // Fase C >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        System.out.println();
        System.out.println("=== Fase C: reiniciar contadores y indice.obtener(70) ===");

        indice.arbol().reiniciarVisitas();
        indice.tabla().reiniciarSondas();

        String resultado = indice.obtener( 70 );

        System.out.println("  indice.obtener(70) = " + resultado);
        mostrar_estado( indice );

        verificar( "resultado = P70", resultado.equals( "P70" ) );
        verificar( "arbol.visitas() = 0 (el indice no bajo por el arbol)", indice.arbol().visitas() == 0 );
        verificar( "tabla.sondas() = 1 (70 esta solo en su cubeta)", indice.tabla().sondas() == 1 );

        // inmediatamente despues, el mismo obtener pero por el arbol
        String resultado_arbol = indice.arbol().obtener( 70 );

        System.out.println("  arbol.obtener(70) = " + resultado_arbol);
        System.out.println("  arbol.visitas() = " + indice.arbol().visitas() + " | tabla.sondas() = " + indice.tabla().sondas());

        verificar( "arbol.obtener(70) = P70 con 2 visitas (camino 50 -> 70)", resultado_arbol.equals( "P70" ) && indice.arbol().visitas() == 2 );
        verificar( "tabla.sondas() sigue en 1 (arbol.obtener no toca la tabla)", indice.tabla().sondas() == 1 );
        // Fase C -------------------------------------------------------------------------------------------+


        // Fase D >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        System.out.println();
        System.out.println("=== Fase D: indice.eliminar(30) (dos hijos, el sucesor 35 se muda) ===");

        indice.arbol().reiniciarVisitas();
        indice.tabla().reiniciarSondas();

        String eliminado = indice.eliminar( 30 );

        System.out.println("  indice.eliminar(30) = " + eliminado);
        mostrar_estado( indice );

        verificar( "eliminar(30) retorna P30", eliminado.equals( "P30" ) );
        verificar( "arbol: 20(1) 35(4) 40(2) 41(1) 50(10) 60(3) 61(1) 65(2) 70(5) 80(1)",
                    indice.arbol().toString().equals( "20(1) 35(4) 40(2) 41(1) 50(10) 60(3) 61(1) 65(2) 70(5) 80(1)" ) );
        verificar( "la cubeta 8 se queda solo con 41", indice.tabla().dump().indexOf( "[ 8] 41\n" ) >= 0 );
        verificar( "contiene(30) es falso en el indice y en el arbol", !indice.contiene( 30 ) && !indice.arbol().contiene( 30 ) );
        verificar( "kEsimo(2) = 35 (el nodo que se mudo)", indice.kEsimo( 2 ) == 35 );
        verificar( "indice.obtener(35) = P35", indice.obtener( 35 ).equals( "P35" ) );
        verificar( "size arbol = size tabla = 10", indice.arbol().size() == 10 && indice.tabla().size() == 10 );
        verificar( "tamanosConsistentes() = true", indice.arbol().tamanosConsistentes() );
        // Fase D -------------------------------------------------------------------------------------------+


        // una clave que ya no esta tiene que lanzar la excepcion chequeada
        try {
            indice.obtener( 30 );
            verificar( "obtener(30) despues de eliminar lanza ClaveInexistenteException", false );
        } catch ( ClaveInexistenteException e ) {
            verificar( "obtener(30) despues de eliminar lanza ClaveInexistenteException", true );
        }

        System.out.println();
        if ( errores == 0 ) { System.out.println("Traza completa: todas las verificaciones OK"); }
        else { System.out.println("Traza con " + errores + " verificacion(es) en ERROR"); }



        // Experimento 1 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // indice doble CON rehash: se inserta la permutacion de semilla 2026 y despues se hace
        // obtener de esas mismas claves, una vez por el arbol y una vez por el indice
        System.out.println();
        System.out.println("=== Experimento 1: indice doble con rehash (permutacion de 1..N, semilla 2026) ===");
        System.out.println("N | vis_ABB_get | sondas_hash_get | alfa | m");

        for ( int n = 2000; n <= 10000; n += 2000 ) {
            int[] a = permutacion( n );

            IndiceDoble<Integer, String> doble = new IndiceDoble<Integer, String>();
            for ( int i = 0; i < n; i++ ) { doble.agregar( a[i], "P" + a[i] ); }

            doble.arbol().reiniciarVisitas();
            doble.tabla().reiniciarSondas();

            for ( int i = 0; i < n; i++ ) { doble.arbol().obtener( a[i] ); }    // por el arbol
            for ( int i = 0; i < n; i++ ) { doble.obtener( a[i] ); }            // por el indice (tabla)

            System.out.println( n + " | " + doble.arbol().visitas() + " | " + doble.tabla().sondas()
                                + " | " + redondear( doble.tabla().factorCarga() ) + " | " + doble.tabla().capacidad() );
        }
        // Experimento 1 -------------------------------------------------------------------------------------+



        // Experimento 2 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // tabla sola con m = 97 fijo y sin rehash (alfaMax = infinito): se inserta 1..N y se busca 1..N
        System.out.println();
        System.out.println("=== Experimento 2: tabla sola, m = 97, sin rehash ===");
        System.out.println("N | alfa | sondas/N");

        for ( int n = 2000; n <= 10000; n += 2000 ) {
            TablaEncadenada<Integer, Integer> tabla = new TablaEncadenada<Integer, Integer>( 97, Double.POSITIVE_INFINITY );

            for ( int i = 1; i <= n; i++ ) { tabla.insertar( i, i ); }

            tabla.reiniciarSondas();
            for ( int i = 1; i <= n; i++ ) { tabla.obtener( i ); }

            System.out.println( n + " | " + redondear( tabla.factorCarga() ) + " | " + redondear( (double) tabla.sondas() / n ) );
        }
        // Experimento 2 -------------------------------------------------------------------------------------+

    } // <-> end main method


} // <> end TestIndiceDoble class
