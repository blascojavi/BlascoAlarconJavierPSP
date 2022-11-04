package ud2.practices.tasks;

public class ManagerTasks {
    public static void main(String[] args) {
        Team frontend = new Team("Frontend");
        ManagerThread frontendManager = new ManagerThread(frontend);

        EmployeeThread f1 = new EmployeeThread("Pere");
        f1.addTask("Main page design", 2500);
        f1.addTask("Shopping cart navigation bar", 5500);
        frontend.addEmployee(f1);

        EmployeeThread f2 = new EmployeeThread("Maria");
        f2.addTask("Breadcrumb", 1500);
        f2.addTask("Profile page", 7500);
        frontend.addEmployee(f2);

        Team backend = new Team("Backend");
        ManagerThread backendManager = new ManagerThread(backend);

        EmployeeThread b1 = new EmployeeThread("Anna");
        b1.addTask("Connect API to database", 3000);
        b1.addTask("Shopping API models", 4200);
        backend.addEmployee(b1);

        EmployeeThread b2 = new EmployeeThread("Arnau");
        b2.addTask("API authentication", 2100);
        b2.addTask("Testing API", 5500);
        backend.addEmployee(b2);

        Team database = new Team("Database");
        ManagerThread databaseManager = new ManagerThread(database);

        EmployeeThread d1 = new EmployeeThread("Mireia");
        d1.addTask("Relation Model", 5000);
        d1.addTask("Define models in the database", 4200);
        database.addEmployee(d1);

        EmployeeThread d2 = new EmployeeThread("Mar");
        d2.addTask("Install DBMS", 3000);
        d2.addTask("Views", 2800);
        database.addEmployee(d2);

        // TODO: Start all the teams and wait for them to finish their tasks

        System.out.println("Projecte acabat! Tots els equips han acabat les tasques assignades.");
    }
}
