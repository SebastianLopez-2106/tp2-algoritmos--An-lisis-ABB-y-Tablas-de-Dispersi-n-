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

public class TestABBAumentado {

    public static void main(String[] args) throws ClaveInexistenteException {
        ABBAumentado<Integer, String> arbol = new ABBAumentado<>();

        System.out.println("=== FASE A: Inserciones ===");
        int[] claves = {50, 30, 70, 20, 40, 60, 80, 35, 65};
        System.out.println("Operacion\t\ttoString\t\t\t\t\t\t\t\t\t\t\t\taltura");
        System.out.println("(inicial)\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + arbol.altura());
        for (int c : claves) {
            arbol.agregar(c, "P" + c);
            System.out.println("agregar(" + c + ")\t\t" + arbol.toString() + "\t\t\t\t" + arbol.altura() + "\tsize: " + arbol.size() + "\tconsistentes: " + arbol.tamanosConsistentes());
        }

        System.out.println("\n=== FASE B: Consultas ===");
        arbol.reiniciarVisitas();
        System.out.println("kEsimo(6) -> " + arbol.kEsimo(6) + " | visitas: " + arbol.visitas());

        arbol.reiniciarVisitas();
        System.out.println("cuantosMenores(65) -> " + arbol.cuantosMenores(65) + " | visitas: " + arbol.visitas());

        arbol.reiniciarVisitas();
        System.out.println("cuantosMenores(35) -> " + arbol.cuantosMenores(35) + " | visitas: " + arbol.visitas());

        arbol.reiniciarVisitas();
        System.out.println("consultarRango(35, 65) -> " + arbol.consultarRango(35, 65) + " | visitas: " + arbol.visitas());

        arbol.reiniciarVisitas();
        System.out.println("rangoIngenuo(35, 65) -> " + arbol.consultarRangoIngenuo(35, 65) + " | visitas: " + arbol.visitas());

        System.out.println("sucesor(40) -> " + arbol.sucesor(40));
        System.out.println("sucesor(80) -> " + arbol.sucesor(80));
        System.out.println("predecesor(35) -> " + arbol.predecesor(35));
        System.out.println("rango(50) -> " + arbol.rango(50));

        System.out.println("\n=== FASE C: Eliminar ===");
        arbol.reiniciarVisitas();
        System.out.println("eliminar(30) -> " + arbol.eliminar(30));
        System.out.println(arbol.toString() + "\t\taltura: " + arbol.altura() + "\tsize: " + arbol.size() + "\tconsistentes: " + arbol.tamanosConsistentes());

        System.out.println("\nRecorrido For-Each (Iterador):");
        for (Integer clave : arbol) {
            System.out.print(clave + " ");
        }
        System.out.println();

        System.out.println("\n=== FASE D: Tabla de visitas (Experimento) ===");
        System.out.println("N\t\th_aleat\th_ord\tvis_kEsimo_aleat\tvis_kEsimo_ord\tvis_rango_aum\tvis_rango_ing");
        int[] N_vals = {2000, 4000, 6000, 8000, 10000};

        for (int N : N_vals) {
            ABBAumentado<Integer, String> arb_aleat = new ABBAumentado<>();
            ABBAumentado<Integer, String> arb_ord = new ABBAumentado<>();

            // Ordenado
            for (int i = 1; i <= N; i++) {
                arb_ord.agregar(i, "V");
            }

            // Aleatorio (Fisher-Yates)
            Random rng = new Random(2026);
            int[] arr = new int[N];
            for (int i = 0; i < N; i++) arr[i] = i + 1;
            for (int i = N - 1; i > 0; i--) {
                int j = rng.nextInt(i + 1);
                int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
            for (int i = 0; i < N; i++) {
                arb_aleat.agregar(arr[i], "V");
            }

            int h_aleat = arb_aleat.altura();
            int h_ord = arb_ord.altura();

            arb_aleat.reiniciarVisitas();
            arb_aleat.kEsimo(N / 2);
            long vis_kEsimo_aleat = arb_aleat.visitas();

            arb_ord.reiniciarVisitas();
            arb_ord.kEsimo(N / 2);
            long vis_kEsimo_ord = arb_ord.visitas();

            Integer k1 = arb_aleat.kEsimo(N / 4);
            Integer k2 = arb_aleat.kEsimo(3 * N / 4);

            arb_aleat.reiniciarVisitas();
            arb_aleat.consultarRango(k1, k2);
            long vis_rango_aum = arb_aleat.visitas();

            arb_aleat.reiniciarVisitas();
            arb_aleat.consultarRangoIngenuo(k1, k2);
            long vis_rango_ing = arb_aleat.visitas();

            System.out.println(N + "\t" + h_aleat + "\t\t" + h_ord + "\t" + vis_kEsimo_aleat + "\t\t\t\t\t" + vis_kEsimo_ord + "\t\t\t\t" + vis_rango_aum + "\t\t\t\t" + vis_rango_ing);
        }
    }
}
