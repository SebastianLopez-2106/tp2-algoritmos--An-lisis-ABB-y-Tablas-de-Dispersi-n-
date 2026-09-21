# Trabajo Práctico 2 – U2-U3 (Análisis, ABB y Tablas de Dispersión)

**Grupo:** g_tq24
**Integrantes:**
- Lopez Guerreros, Sebastian Alejandro, 6.153.672, TQ
- Oviedo Fernandez, Blas Nazario, 7.037.075, TQ

---

## Análisis Asintótico - Ejercicio 1 (ABBAumentado)

### 1. Recurrencia de `kEsimo` en función de la altura
En el peor de los casos, la función `kEsimo` desciende por una sola rama del árbol hasta llegar a una hoja, realizando un número constante de operaciones en cada nodo (comparaciones y sumas simples).
La recurrencia es:
$$T(h) = T(h-1) + \Theta(1)$$
$$T(1) = \Theta(1)$$

**Solución:** Desenrollando la recurrencia tenemos $T(h) = T(h-1) + c = T(h-2) + 2c = ... = T(1) + (h-1)c$. Por lo tanto, el tiempo de ejecución es $\Theta(h)$.

### 2. Búsqueda y `kEsimo` en un ABB perfectamente balanceado
En un árbol perfectamente balanceado, cada descenso descarta exactamente la mitad de los nodos restantes. La cantidad de nodos de los subárboles es $n/2$.
La recurrencia es:
$$T(n) = T(n/2) + \Theta(1)$$
$$T(1) = \Theta(1)$$

**Solución por Teorema Maestro:** Identificamos $a=1$, $b=2$, $f(n) = \Theta(1)$. Comparamos $f(n)$ con $n^{\log_b a} = n^{\log_2 1} = n^0 = 1$. Dado que $f(n) = \Theta(n^{\log_b a})$, nos encontramos en el **Caso 2** del Teorema Maestro. 
Concluimos que:
$$T(n) = \Theta(n^{\log_2 1} \log n) = \Theta(\log n)$$

### 3. Búsqueda en un ABB degenerado (inserción ordenada)
Si los elementos se insertan en orden, el árbol se convierte en una lista enlazada. Cada nodo tiene un solo hijo. 
La recurrencia en función de los nodos es:
$$T(n) = T(n-1) + \Theta(1)$$

**Solución mediante desenrollado:**
$$T(n) = T(n-1) + c$$
$$T(n) = T(n-2) + 2c$$
$$...$$
$$T(n) = T(1) + (n-1)c$$
Dado que el subproblema disminuye en una unidad (y no se divide en subproblemas de tamaño $n/b$), el Teorema Maestro no aplica. Concluimos que el tiempo es $\Theta(n)$.

### 4. Complejidad de `consultarRango`
*   **`consultarRango` (Aumentado):** Al utilizar nuestra implementación basada en `CountSmallerA` y `CountSmallerB`, la función desciende por, a lo sumo, dos ramas del árbol para encontrar los límites del rango. Esto toma tiempo $\Theta(h)$. Como la implementación es recursiva, utiliza espacio extra en la pila de llamadas de $\Theta(h)$.
*   **`consultarRangoIngenuo`:** Al recorrer todo el árbol mediante un recorrido inorden y sumar aquellos nodos que caen dentro del rango, se visitan obligatoriamente todos los nodos. Su complejidad en tiempo es siempre $\Theta(n)$.

### 5. Coste Espacial del Árbol
El espacio utilizado por el árbol es $\Theta(n)$. La adición de la variable entera `tamano` a cada nodo representa un incremento espacial de $\Theta(1)$ por cada nodo. Esto multiplica el espacio total por una constante pequeña, pero no altera el orden de crecimiento asintótico, manteniéndose firmemente en $\Theta(n)$.

### 6. Relación con la Tabla de Visitas
Los resultados del programa de pruebas verifican la teoría:
*   En el árbol ordenado, `h_ord` vale exactamente $N-1$ y `vis_kEsimo_ord` vale $N/2$, comprobando el comportamiento lineal $\Theta(n)$.
*   En el árbol aleatorio, la altura `h_aleat` se mantiene logarítmica (muy inferior a $N$).
*   Se evidencia la superioridad del árbol aumentado: `vis_rango_aum` se mantiene en pocas decenas (proporcional a la altura $h$), mientras que `vis_rango_ing` siempre es igual a $N$, lo que demuestra el altísimo coste $\Theta(n)$ de no usar el invariante de tamaño.

---

## Análisis Asintótico - Ejercicio 2 (Índice Doble)

### 7. Búsqueda con Encadenamiento
*   **Tiempo esperado:** $\Theta(1+\alpha)$, donde $\alpha = n/m$ es el factor de carga. Si el hash es uniforme, la longitud esperada de cada cadena es $\alpha$.
*   **Peor caso:** $\Theta(n)$, lo cual sucede si todas las claves colisionan y caen en la misma cubeta (formando una sola lista enlazada).
*   **Espacio:** $\Theta(n+m)$ para almacenar los $m$ punteros del arreglo base y los $n$ nodos dispersos en las listas.

### 8. `agregar` del Índice Doble
*   **Caso promedio:** Para agregar, primero se inserta en el árbol ($\Theta(h)$) y luego en la tabla ($\Theta(1+\alpha)$ en promedio, ya que se revisa la cadena buscando si la clave existe). Total: $\Theta(h) + \Theta(1+\alpha)$.
*   **Peor caso (Rehash):** Si la inserción provoca que $\alpha > \alpha_{max}$, la operación toma $\Theta(n)$ porque se reubican todos los $n$ elementos. Sin embargo, como $m$ se duplica en cada expansión, el coste total de todos los rehash sobre $n$ inserciones es estrictamente menor a $2n$. Esto significa que el coste amortizado por operación para la tabla se mantiene en $O(1)$.

### 9. Lectura de las tablas del Experimento 2
*   **Experimento 1 (Con Rehash):** Como la tabla controla el factor de carga ($\alpha \le 1$), las `sondas_hash_get` se mantienen planas alrededor de 1 sonda por búsqueda. El tiempo real está dominado completamente por el $\Theta(1)$ de la tabla, a diferencia del árbol.
*   **Experimento 2 (Sin Rehash, $m=97$ fijo):** Al insertar $N$ elementos en un tamaño fijo $m=97$, el factor de carga $\alpha$ crece linealmente. La columna `sondas/N` muestra un crecimiento proporcional directo a $\alpha$. Esto valida en la práctica que el tiempo de búsqueda con encadenamiento es exactamente $\Theta(1+\alpha)$.

---

## Uso de Inteligencia Artificial Generativa

En cumplimiento de las normas de integridad, declaramos que se utilizó Inteligencia Artificial Generativa (LLM) durante el desarrollo de este trabajo práctico para los siguientes propósitos:

1.  **Redacción de este documento:** Asistencia en el formateo y estructuración formal de este `README.md`, particularmente en la redacción de las justificaciones asintóticas y el tipeo de las fórmulas matemáticas en notación $\LaTeX$.
2.  **Asistencia en depuración (Debugging):** Análisis de errores estructurales previos al envío.
3.  **Generación de código de prueba:** Asistencia en la elaboración y pulido de las clases auxiliares y del código que ejecuta las pruebas empíricas formales mostradas en la consola.
