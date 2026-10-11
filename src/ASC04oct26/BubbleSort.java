package ASC04oct26;

	public class BubbleSort {

	    // Enumerado para definir el tipo de orden
	    public enum Orden {
	        CRECIENTE,
	        DECRECIENTE
	    }

	    /**
	     * Ordena un array de tipo double utilizando el algoritmo Bubble Sort.
	     */
	    public static void ordenar(double[] array, Orden orden) {
	        if (array == null || array.length <= 1) {
	            return; // No es necesario ordenar un array vacío o de un solo elemento
	        }

	        int n = array.length;
	        boolean intercambiado;

	        // Bucle externo para controlar las pasadas
	        for (int i = 0; i < n - 1; i++) {
	            intercambiado = false;

	            // Bucle interno para comparar elementos adyacentes
	            for (int j = 0, z =3; j < n - i - 1; j++) {
	                boolean bIntercambiar = false;

	                // Evaluamos la condición según el orden solicitado
	                if (orden == Orden.CRECIENTE) {
	                    if (array[j] > array[j + 1]) {
	                        bIntercambiar = true;
	                    }
	                } else if (orden == Orden.DECRECIENTE) {
	                    if (array[j] < array[j + 1]) {
	                        bIntercambiar = true;
	                    }
	                }

	                // Si se cumple la condición, realizamos el intercambio
	                if (bIntercambiar) {
	                    double temporal = array[j];
	                    array[j] = array[j + 1];
	                    array[j + 1] = temporal;
	                    intercambiado = true; // Marcamos que hubo un cambio en esta pasada
	                }
	            }

	            // Optimización: si no hubo intercambios, el array ya está ordenado
	            if (!intercambiado) {
	                break;
	            }
	        }
	    }

	    // Método principal (Main) para probar el funcionamiento
	    public static void main(String[] args) {
	        double[] miArray = {4.7, 2.2, 10.8, -3.3, 7.5, -1.5};

	        System.out.println("Array original:");
	        imprimirArray(miArray);

	        // Ordenar de forma Creciente
	        BubbleSort.ordenar(miArray, Orden.CRECIENTE);
	        System.out.println("\nOrden Creciente:");
	        imprimirArray(miArray);

	        // Ordenar de forma Decreciente
	        BubbleSort.ordenar(miArray, Orden.DECRECIENTE);
	        System.out.println("\nOrden Decreciente:");
	        imprimirArray(miArray);
	    }

	    // Método auxiliar para imprimir el array en consola
	    private static void imprimirArray(double[] array) {
	        for (double elemento : array) {
	            System.out.print(elemento + "  ");
	        }
	        System.out.println();
	    }
	}

