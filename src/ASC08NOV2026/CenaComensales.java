package ASC08NOV2026;

public class CenaComensales {

    // Representa un cubierto genérico (tenedor o cuchillo)
    static class Cubierto {
        private final int id;
        private final String tipo;

        public Cubierto(int id, String tipo) {
            this.id = id;
            this.tipo = tipo;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return tipo + " #" + id;
        }
    }

    // Representa al comensal (filósofo)
    static class Comensal implements Runnable {
        private final int id;
        private final Cubierto primerCubierto;
        private final Cubierto segundoCubierto;

        public Comensal(int id, Cubierto tenedor, Cubierto cuchillo) {
            this.id = id;
            // Estrategia anti-deadlock: ordenar la adquisición por ID único
            if (tenedor.getId() < cuchillo.getId()) {
                this.primerCubierto = tenedor;
                this.segundoCubierto = cuchillo;
            } else {
                this.primerCubierto = cuchillo;
                this.segundoCubierto = tenedor;
            }
        }

        private void pensar() throws InterruptedException {
            System.out.println("Comensal " + id + " está pensando...");
            Thread.sleep((long) (Math.random() * 1000));
        }

        private void comer() throws InterruptedException {
            System.out.println("--> Comensal " + id + " está COMIENDO con " + primerCubierto + " y " + segundoCubierto);
            Thread.sleep((long) (Math.random() * 1000));
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    pensar();

                    System.out.println("Comensal " + id + " intenta tomar " + primerCubierto);
                    // Bloqueo del primer cubierto (ID menor)
                    synchronized (primerCubierto) {
                        System.out.println("Comensal " + id + " tiene " + primerCubierto + ". Intenta tomar " + segundoCubierto);
                        
                        // Bloqueo del segundo cubierto (ID mayor)
                        synchronized (segundoCubierto) {
                            comer();
                        }
                        System.out.println("<-- Comensal " + id + " soltó " + segundoCubierto);
                    }
                    System.out.println("<-- Comensal " + id + " soltó " + primerCubierto);
                }
            } catch (InterruptedException e) {
                System.out.println("Comensal " + id + " fue interrumpido y deja la mesa.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void iniciarCena(int numComensales) {
        Cubierto[] tenedores = new Cubierto[numComensales];
        Cubierto[] cuchillos = new Cubierto[numComensales];
        Thread[] hilos = new Thread[numComensales];

        // Inicializar cubiertos con identificadores globales impares y pares para garantizar unicidad
        for (int i = 0; i < numComensales; i++) {
            tenedores[i] = new Cubierto(i * 2, "Tenedor");
            cuchillos[i] = new Cubierto((i * 2) + 1, "Cuchillo");
        }

        // Crear y lanzar los hilos de los comensales
        for (int i = 0; i < numComensales; i++) {
            // Cada comensal se sienta entre un tenedor y un cuchillo asignado alternadamente
            Cubierto tenedor = tenedores[i];
            Cubierto cuchillo = cuchillos[(i + 1) % numComensales];

            Comensal comensal = new Comensal(i, tenedor, cuchillo);
            hilos[i] = new Thread(comensal);
            hilos[i].start();
        }

        // Dejar correr la simulación por 10 segundos
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Finalización limpia del programa
        System.out.println("\n--- Finalizando la cena de forma controlada ---");
        for (Thread hilo : hilos) {
            hilo.interrupt();
        }
    }

    public static void main(String[] args) {
        iniciarCena(5);
    }
}
