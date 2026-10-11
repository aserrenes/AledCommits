package ASC110CT26;

public class MergeSort {

	   // Método principal público que el usuario llama
    public static void ordenar(int[] array) {
        if (array == null || array.length <= 1) {
            return; // El array ya está ordenado o está vacío
        }
        mergeSort(array, 0, array.length - 1);
    }

    // Método recursivo que divide el array
    private static void mergeSort(int[] array, int izquierda, int derecha) {
        if (izquierda < derecha) {
            // Encuentra el punto medio para dividir el array
            int medio = izquierda + (derecha - izquierda) / 2;

            // Divide y vencerás: Ordena la primera y la segunda mitad
            mergeSort(array, izquierda, medio);
            mergeSort(array, medio + 1, derecha);

            // Une (mezcla) las dos mitades ordenadas
            combinar(array, izquierda, medio, derecha);
        }
    }

    // Método que combina (mezcla) dos sub-arrays de forma ordenada
    private static void combinar(int[] array, int izquierda, int medio, int derecha) {
        // Tamaños de los dos sub-arrays a unir
        int n1 = medio - izquierda + 1;
        int n2 = derecha - medio;

        // Creación de arrays temporales
        int[] izquierdaArray = new int[n1];
        int[] derechaArray = new int[n2];

        // Copiar los datos a los arrays temporales
        for (int i = 0; i < n1; ++i) {
            izquierdaArray[i] = array[izquierda + i];
        }
        for (int j = 0; j < n2; ++j) {
            derechaArray[j] = array[medio + 1 + j];
        }

        // Índices iniciales para recorrer los sub-arrays y el array principal
        int i = 0, j = 0;
        int k = izquierda;

        // Compara y mezcla los elementos en orden ascendente
        while (i < n1 && j < n2) {
            if (izquierdaArray[i] <= derechaArray[j]) {
                array[k] = izquierdaArray[i];
                i++;
            } else {
                array[k] = derechaArray[j];
                j++;
            }
            k++;
        }

        // Copia los elementos restantes de izquierdaArray si queda alguno
        while (i < n1) {
            array[k] = izquierdaArray[i];
            i++;
            k++;
        }

        // Copia los elementos restantes de derechaArray si queda alguno
        while (j < n2) {
            array[k] = derechaArray[j];
            j++;
            k++;
        }
    }

    // Método principal (Main) para probar el funcionamiento
    public static void main(String[] args) {
        int[] numeros = {38, 27, 43, 3, 9, 82, 10};

        System.out.println("Array original:");
        imprimirArray(numeros);

        // Llamada al método de ordenamiento
        MergeSort.ordenar(numeros);

        System.out.println("\nArray ordenado:");
        imprimirArray(numeros);
    }

    // Método auxiliar para imprimir el array de forma visual
    private static void imprimirArray(int[] array) {
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

}
