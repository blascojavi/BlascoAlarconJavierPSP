package ud2.practices.tasks;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<EmployeeThread> employees;

    public Team(String name) {
        this.name = name;
        employees = new ArrayList<>();
    }

    public void addEmployee(EmployeeThread employee) {
        this.employees.add(employee);
    }

    public List<EmployeeThread> getEmployees() {
        return employees;
    }

    public String getName() {
        return name;
    }
}
