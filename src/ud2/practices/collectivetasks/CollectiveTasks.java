package ud2.practices.collectivetasks;



public class CollectiveTasks {


    //cada equipo tiene que tener una bolsa de tareas (frontend, backend y database)
    //cada empleado del equipo tomara una tarea y la realizara(las tareas deben de pasar por dos estados, antes de darlas por finalizadas)
    //Se han de implementar los metodos ->
    // Task::work Define cómo se completa una tarea. Obtiene el hilo actual y hace que espere tantos milisegundos como se ha definido en la tarea.
    //Este método imprimirá un mensaje cuando comience la tarea y cuando se finalice:


    public static void main(String[] args) {
        Team frontend = new Team("Frontend");
        frontend.addTask("Main page design", 2500);
        frontend.addTask("Shopping cart navigation bar", 5500);
        frontend.addTask("Breadcrumb", 1500);
        frontend.addTask("Profile page", 7500);
        frontend.addEmployee(new EmployeeThread("Pere"));
        frontend.addEmployee(new EmployeeThread("Maria"));

        Team backend = new Team("Backend");
        backend.addTask("Connect API to database", 3000);
        backend.addTask("Shopping API models", 4200);
        backend.addTask("API authentication", 2100);
        backend.addTask("Testing API", 5500);
        backend.addEmployee(new EmployeeThread("Anna"));
        backend.addEmployee(new EmployeeThread("Arnau"));

        Team database = new Team("Database");
        database.addTask("Relation Model", 5000);
        database.addTask("Define models in the database", 4200);
        database.addTask("Install DBMS", 3000);
        database.addTask("Views", 2800);
        database.addEmployee(new EmployeeThread("Mireia"));
        database.addEmployee(new EmployeeThread("Mar"));

        // TODO: Start all the teams and wait for them to finish their tasks


        for (EmployeeThread e: frontend.getEmployees()){
            e.start();

            //e.team.removeTask(e.team.getNextTask().getName(),e.team.getNextTask().getDuration());

            //System.out.println(e.getState().name());

            //frontend.getNextTask();
            //System.out.println(e.team.getName());
        }
        /*
        for (EmployeeThread e: backend.getEmployees()){
            e.start();
        }
        for (EmployeeThread e: database.getEmployees()){
            e.start();
        }
        */
        try{
            for (EmployeeThread e: frontend.getEmployees()){
                e.join();
                //e.team.removeTask(e.team.getNextTask().getName(),e.team.getNextTask().getDuration());

            }
            /*
            for (EmployeeThread e: backend.getEmployees()){
                e.join();
            }
            for (EmployeeThread e: database.getEmployees()){
                e.join();
            }
             */

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Projecte acabat! Tots els equips han acabat les tasques assignades.");
    }














}