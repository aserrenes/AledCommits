package ASC2026_10_25;

public class GestionAscensor {
	
	import java.util.LinkedList;
	import java.util.Queue;
	import java.util.concurrent.locks.Condition;
	import java.util.concurrent.locks.Lock;
	import java.util.concurrent.locks.ReentrantLock;

	// Clase que actúa como el búfer compartido (Región Crítica)
	class ControladorAscensor {
	    private final Queue<Integer> colaPeticiones = new LinkedList<>();
	    private final int CAPACIDAD_MAXIMA = 5; // Límite de peticiones pendientes en la cola

	    // Uso de cerrojo explícito para exclusión mutua
	    private final Lock cerrojo = new ReentrantLock();
	    
	    // Condiciones para controlar la sincronización fina
	    private final Condition colaLlena = cerrojo.newCondition();
	    private final Condition colaVacia = cerrojo.newCondition();

	    // Método que utiliza el Productor (Petición de piso)
	    public void solicitarPiso(int piso) {
	        cerrojo.lock(); // Entrar a la región crítica
	        try {
	            // Si la cola está llena, el productor espera
	            while (colaPeticiones.size() == CAPACIDAD_MAXIMA) {
	                System.out.println("[⚠️ Sistema] Cola de peticiones llena. Esperando para añadir piso: " + piso);
	                colaLlena.await();
	            }

	            // Añadir la petición a la región crítica
	            colaPeticiones.add(piso);
	            System.out.println("[🔔 Persona] Se ha solicitado el piso: " + piso + " (Peticiones en cola: " + colaPeticiones.size() + ")");

	            // Notificar al consumidor (Ascensor) que ya hay trabajo disponible
	            colaVacia.signalAll();

	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        } finally {
	            cerrojo.unlock(); // Asegurar siempre la liberación del cerrojo
	        }
	    }

	    // Método que utiliza el Consumidor (El ascensor procesa el piso)
	    public int atenderPeticion() {
	        cerrojo.lock(); // Entrar a la región crítica
	        try {
	            // Si no hay peticiones, el ascensor se duerme/espera
	            while (colaPeticiones.isEmpty()) {
	                System.out.println("[💤 Ascensor] No hay peticiones. Ascensor en reposo...");
	                colaVacia.await();
	            }

	            // Extraer y procesar el piso de la región crítica
	            int pisoDestino = colaPeticiones.poll();
	            System.out.println("[🚀 Ascensor] Moviéndose hacia el piso: " + pisoDestino);

	            // Notificar a los productores que hay espacio libre en la cola
	            colaLlena.signalAll();
	            
	            return pisoDestino;

	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            return -1;
	        } finally {
	            cerrojo.unlock(); // Asegurar siempre la liberación del cerrojo
	        }
	    }
	}

	// Hilo Productor: Simula personas presionando los botones del ascensor
	class PersonaProductor implements Runnable {
	    private final ControladorAscensor controlador;

	    public PersonaProductor(ControladorAscensor controlador) {
	        this.controlador = controlador;
	    }

	    @Override
	    public void run() {
	        int[] pisosSimulados = {3, 7, 1, 0, 9, 4, 2}; // Pisos que pedirán las personas
	        for (int piso : pisosSimulados) {
	            controlador.solicitarPiso(piso);
	            try {
	                // Simula el tiempo que tarda otra persona en llegar y pulsar un botón
	                Thread.sleep((long) (Math.random() * 800));
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	}

	// Hilo Consumidor: Simula el movimiento mecánico del ascensor
	class AscensorConsumidor implements Runnable {
	    private final ControladorAscensor controlador;

	    public AscensorConsumidor(ControladorAscensor controlador) {
	        this.controlador = controlador;
	    }

	    @Override
	    public void run() {
	        while (!Thread.currentThread().isInterrupted()) {
	            int pisoAtendido = controlador.atenderPeticion();
	            if (pisoAtendido == -1) break;

	            try {
	                // Simula el viaje mecánico entre pisos y la apertura de puertas
	                Thread.sleep(1500);
	                System.out.println("[✅ Ascensor] Llegó al piso: " + pisoAtendido);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	}

	// Clase Principal para ejecutar la simulación
	public class MainAscensor {
	    public static void main(String[] args) {
	        // Región crítica compartida
	        ControladorAscensor controlador = new ControladorAscensor();

	        // Crear los hilos (Productor y Consumidor)
	        Thread hiloAscensor = new Thread(new AscensorConsumidor(controlador));
	        Thread hiloPersonas = new Thread(new PersonaProductor(controlador));

	        // Iniciar la ejecución de la concurrencia
	        hiloAscensor.start();
	        hiloPersonas.start();

	        // Dejar correr la simulación un momento y luego detenerla de forma segura
	        try {
	            hiloPersonas.join(); // Esperar a que el productor termine de meter peticiones
	            Thread.sleep(5000);   // Dar tiempo a que el ascensor termine las últimas pendientes
	            hiloAscensor.interrupt(); // Detener el bucle infinito del ascensor
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        
	        System.out.println("[🏁 Sistema] Simulación finalizada de forma segura.");
	    }
	}
