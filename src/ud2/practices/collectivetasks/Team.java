package ud2.practices.collectivetasks;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<EmployeeThread> employees;
    private List<Task> unfinishedTasks;
    private List<Task> testingTasks = new ArrayList<>();
    private List<Task> finishedTasks = new ArrayList<>();

    public Team(String name) {
        this.name = name;
        employees = new ArrayList<>();
        unfinishedTasks = new ArrayList<>();
    }

    public void addEmployee(EmployeeThread employee) {
        this.employees.add(employee);
        employee.setTeam(this);
    }

    public void addTask(String taskName, int taskDuration) {
        unfinishedTasks.add(new Task(taskName, taskDuration));
    }


    public List<EmployeeThread> getEmployees() {

        return employees;
    }

    public String getName() {
        return name;
    }




    public Task getNextTask(){
        /**
         * TODO:
         * Get next Task from unfinishedTasks. If all unfinishedTaks are done,
         * get next Task from testingTasks.
         * The task must be deleted from the list when retrieved.
         * If all Tasks are done and tested, return null.
         */
        if (unfinishedTasks.size() !=0) {
            return unfinishedTasks.remove(0);
        }
        if (testingTasks.size() != 0){
            return testingTasks.remove(0);
        }
        return null;
    }
/*
        if (unfinishedTasks.size() >0){
            for (Task t: unfinishedTasks){

                finishedTasks.add(t);
                unfinishedTasks.remove(0);
                addTestingTask(t);
                //unfinishedTasks.remove(new Task(t.getName(), t.getDuration()));
                return t;
            }
        }else if ( testingTasks.size()>0){
            for (Task t: testingTasks){
                return t;
            }
        }

        return null;

    }

 */
    public void addTestingTask(Task t){
        testingTasks.add(t);
    }
    public void addFinishedTask(Task t){
        finishedTasks.add(t);
    }
}