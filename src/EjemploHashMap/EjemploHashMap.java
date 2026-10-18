package EjemploHashMap;

import java.util.HashMap;
import java.util.Map;

public class EjemploHashMap {
    public static void main(String[] args) {
        // 1. Crear el HashMap (Clave: String [DNI/ID], Valor: String [Nombre])
        Map<String, String> empleados = new HashMap<>();

        // 2. Insertar datos (clave, valor)
        empleados.put("EMP01", "Ana Gómez");
        empleados.put("EMP02", "Carlos Pérez");
        empleados.put("EMP03", "María López");

        // 3. Intentar insertar una clave duplicada (reemplazará el valor anterior)
        empleados.put("EMP02", "Carlos Ruiz"); 

        // 4. Obtener un valor específico usando su clave
        String empleado = empleados.get("EMP03");
        System.out.println("El empleado con ID EMP03 es: " + empleado);

        // 5. Recorrer e imprimir todo el HashMap
        System.out.println("\n--- Lista de Empleados ---");
        for (Map.Entry<String, String> entrada : empleados.entrySet()) {
            System.out.println("ID: " + entrada.getKey() + " | Nombre: " + entrada.getValue());
        }
    }
}
