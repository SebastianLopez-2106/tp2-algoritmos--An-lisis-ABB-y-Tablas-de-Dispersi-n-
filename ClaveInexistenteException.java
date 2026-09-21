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

// Es una excepción chequeada (extiende Exception) porque representa una situación
// que el código que llama puede prever y manejar razonablemente, como buscar un
// paquete que ya se despachó.
public class ClaveInexistenteException extends Exception {
    public ClaveInexistenteException ( String message ) {
        super(message);
    }
}