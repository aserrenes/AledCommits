package ASC01NOV2026;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExample {
	

	    public static void main(String[] args) {
	        // Creamos un pool fijo de 3 hilos de forma tradicional
	        ExecutorService ejecutor = Executors.newFixedThreadPool(3);

	        try {
	            System.out.println("Iniciando el envío de tareas en Java 17...");

	            // Enviamos 5 tareas al ejecutor
	            for (int i = 1; i <= 5; i++) {
	                int idTarea = i;

	                ejecutor.submit(() -> {
	                    String nombreHilo = Thread.currentThread().getName();
	                    System.out.println("-> Tarea " + idTarea + " iniciada por el " + nombreHilo);

	                    try {
	                        // Simulamos que la tarea tarda 2 segundos en completarse
	                        Thread.sleep(2000);
	                    } catch (InterruptedException e) {
	                        System.err.println("Tarea " + idTarea + " interrumpida.");
	                        Thread.currentThread().interrupt();
	                    }

	                    System.out.println("<- Tarea " + idTarea + " COMPLETADA por el " + nombreHilo);
	                });
	            }

	            System.out.println("Todas las tareas han sido enviadas.");

	        } finally {
	            // Buenas prácticas en Java 17: Apagado explícito del ExecutorService
	            System.out.println("Iniciando el apagado del ejecutor...");
	            ejecutor.shutdown(); // No acepta nuevas tareas, pero termina las que están en cola

	            try {
	                // Esperamos un máximo de 60 segundos a que terminen las tareas activas
	                if (!ejecutor.awaitTermination(60, TimeUnit.SECONDS)) {
	                    ejecutor.shutdownNow(); // Cancela tareas pendientes si expira el tiempo
	                }
	            } catch (InterruptedException e) {
	                ejecutor.shutdownNow(); // Cancela todo si el hilo principal es interrumpido
	                Thread.currentThread().interrupt();
	            }
	        }

	        System.out.println("El ExecutorService se ha cerrado. Hilo principal terminado.");
	    }
	}

