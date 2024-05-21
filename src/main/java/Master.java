import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Clase que representa al Esclavo
class Slave implements Callable<String> {
    private final int taskId;

    public Slave(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String call() throws Exception {
        // Simulación de ejecución de una tarea
        Thread.sleep(1000); // Simular tiempo de procesamiento
        return "Resultado de la tarea " + taskId;
    }
}

// Clase que representa al Maestro
public class Master {
    private final int numSlaves;
    private final ExecutorService executor;

    public Master(int numSlaves) {
        this.numSlaves = numSlaves;
        this.executor = Executors.newFixedThreadPool(numSlaves);
    }

    public void executeTasks(List<Integer> tasks) {
        List<Future<String>> results = new ArrayList<>();

        // Distribuir tareas a los esclavos
        for (int taskId : tasks) {
            Slave slave = new Slave(taskId);
            Future<String> result = executor.submit(slave);
            results.add(result);
        }

        // Recoger y mostrar los resultados
        for (Future<String> result : results) {
            try {
                System.out.println(result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Cerrar el ejecutor
        executor.shutdown();
    }

    public static void main(String[] args) {
        List<Integer> tasks = new ArrayList<>();
        // Crear una lista de tareas
        for (int i = 1; i <= 5; i++) {
            tasks.add(i);
        }

        // Crear y ejecutar el maestro
        Master master = new Master(3); // 3 esclavos
        master.executeTasks(tasks);
    }
}
