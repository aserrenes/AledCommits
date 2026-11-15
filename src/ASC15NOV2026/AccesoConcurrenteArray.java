package ASC15NOV2026;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccesoConcurrenteArray {

    // Array compartido
    private final int[] datos;
    // Lock que separa permisos de lectura y escritura
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public AccesoConcurrenteArray(int tamano) {
        this.datos = new int[tamano];
    }

    /**
     * Método de escritura: Bloqueo exclusivo.
     * Nadie más puede leer ni escribir mientras se ejecuta.
     */
    public void escribir(int indice, int valor) {
        rwLock.writeLock().lock();
        try {
            System.out.printf("[%s] -> Escribiendo valor %d en índice %d...%n",
                    Thread.currentThread().getName(), valor, indice);
            
            // Simulamos el tiempo de escritura de 10ms
            Thread.sleep(10);
            datos[indice] = valor;
            
            System.out.printf("[%s] <- Escritura completada.%n",
                    Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Siempre liberar el lock en el bloque finally
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Método de lectura: Bloqueo compartido.
     * Varios hilos pueden leer simultáneamente si no hay nadie escribiendo.
     */
    public int leer(int indice) {
        rwLock.readLock().lock();
        try {
            System.out.printf("[%s] -> Leyendo índice %d...%n",
                    Thread.currentThread().getName(), indice);
            
            // Simulamos el tiempo de lectura de 5ms
            Thread.sleep(5);
            int valor = datos[indice];
            
            System.out.printf("[%s] <- Leído valor %d del índice %d.%n",
                    Thread.currentThread().getName(), valor, indice);
            return valor;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AccesoConcurrenteArray recursoCompartido = new AccesoConcurrenteArray(5);
        
        // Creamos un pool de 6 hilos para simular concurrencia real
        ExecutorService executor = Executors.newFixedThreadPool(6);

        // Lanzamos 2 tareas de escritura (tardarán 10ms cada una)
        executor.submit(() -> recursoCompartido.escribir(0, 100));
        executor.submit(() -> recursoCompartido.escribir(1, 200));

        // Lanzamos 4 tareas de lectura simultáneas (tardarán 5ms cada una)
        executor.submit(() -> recursoCompartido.leer(0));
        executor.submit(() -> recursoCompartido.leer(1));
        executor.submit(() -> recursoCompartido.leer(0));
        executor.submit(() -> recursoCompartido.leer(1));

        // Cerramos el executor y esperamos a que terminen las tareas
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Estado final del array: " + Arrays.toString(recursoCompartido.datos));
    }
}
