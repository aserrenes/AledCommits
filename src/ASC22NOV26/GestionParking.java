package ASC22NOV26;

import java.util.concurrent.ThreadLocalRandom;

public class GestionParking {

    private static final int CAPACIDAD_MAXIMA = 100;
    private int plazasOcupadas = 0;

    // Método sincronizado para registrar la entrada de un vehículo
    public synchronized void entrar(String nombreEntrada) throws InterruptedException {
        // Bucle while obligatorio para evitar despertamientos espurios (spurious wakeups)
        while (plazasOcupadas >= CAPACIDAD_MAXIMA) {
            System.out.printf("[%s] Parking LLENO (%d/%d). Vehículo esperando...%n",
                    nombreEntrada, plazasOcupadas, CAPACIDAD_MAXIMA);
            wait(); // Libera el monitor y espera a que salga algún vehículo
        }

        plazasOcupadas++;
        System.out.printf("--> [%s] Entra vehículo. Ocupación actual: %d/%d%n",
                nombreEntrada, plazasOcupadas, CAPACIDAD_MAXIMA);

        // Notifica a otros hilos (por si el monitor o la salida estaban esperando)
        notifyAll();
    }

    // Método sincronizado para registrar la salida de un vehículo
    public synchronized void salir(String nombreSalida) throws InterruptedException {
        while (plazasOcupadas <= 0) {
            System.out.printf("[%s] Parking VACÍO (0/%d). Esperando coches...%n",
                    nombreSalida, CAPACIDAD_MAXIMA);
            wait(); // Libera el monitor hasta que entre algún vehículo
        }

        plazasOcupadas--;
        System.out.printf("<-- [%s] Sale vehículo. Ocupación actual: %d/%d%n",
                nombreSalida, plazasOcupadas, CAPACIDAD_MAXIMA);

        // Despierta a los hilos de entrada que estuvieran bloqueados por aforo completo
        notifyAll();
    }

    // Método sincronizado para consultar el estado de forma consistente
    public synchronized int getPlazasOcupadas() {
        return plazasOcupadas;
    }

    public static void main(String[] args) {
        Parking parking = new Parking();

        // Hilo 1: Entrada Norte (genera llegadas aleatorias)
        Thread entrada1 = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(150, 500));
                    parking.entrar("Entrada Norte");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Hilo-Entrada-1");

        // Hilo 2: Entrada Sur (genera llegadas aleatorias)
        Thread entrada2 = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(150, 500));
                    parking.entrar("Entrada Sur");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Hilo-Entrada-2");

        // Hilo 3: Salida Única (genera salidas aleatorias)
        Thread salida = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 700));
                    parking.salir("Salida Única");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Hilo-Salida");

        // Hilo 4: Monitorización periódica (Demonio para no impedir el cierre del programa)
        Thread monitor = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(2000);
                    int ocupadas = parking.getPlazasOcupadas();
                    int libres = CAPACIDAD_MAXIMA - ocupadas;
                    System.out.printf("=== [MONITOR] Vehículos dentro: %d | Plazas libres: %d ===%n",
                            ocupadas, libres);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Hilo-Monitor");
        monitor.setDaemon(true);

        // Arrancamos toda la simulación
        entrada1.start();
        entrada2.start();
        salida.start();
        monitor.start();
    }
}
