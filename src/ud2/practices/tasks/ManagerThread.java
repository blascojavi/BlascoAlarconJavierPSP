package ud2.practices.tasks;

public class ManagerThread extends Thread{
    private Team team;

    public ManagerThread(Team team) {
        super();
        this.team = team;
        this.setName(String.format("%sManager", team.getName()));
    }

    @Override
    public void run() {
        // TODO: Make all your assigned employees do their tasks

        for (EmployeeThread empleado: team.getEmployees()) {
            empleado.start();//lanza el hilo
        }
        for (EmployeeThread empleado: team.getEmployees()) {
            try {
                empleado.join();//Espera a que termine
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("%s: L'equip %s ha realitzat totes les tasques.\n", this.getName(), team.getName());
    }
}
