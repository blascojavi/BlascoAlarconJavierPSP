package ud2.practices.collectivetasks;

import java.util.ArrayList;
import java.util.List;

public class EmployeeThread extends Thread{

    Team team;
    public EmployeeThread(String name) {
        super();
        this.setName(name);
        this.team = null;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public void run() {
        /**
         * TODO: Get next task from team.
         *
         * If the task needs to be done:
         *  - Do the task with work()
         *  - Add the task to testingTasks
         * If the task needs to be tested:
         *  - Test the task with test()
         *  - Add the task to finishedTasks
         * If the task is null:
         *  - All tasks are done. Exit.
         */

        //============================

       // int numTareas = team. team.getEmployees().size();

        for (int i=0; i < 2 ; i++){

            Task tk = team.getNextTask();

            if (tk != null){
                Thread current = Thread.currentThread();
                //Task t= team.getNextTask();
                if (tk.status()==TaskStatus.UNFINISHED){
                    tk.work();

                    tk.test();
                    //team.addFinishedTask(t);

                    //t.setStatus(TaskStatus.TESTING);

                    //System.out.println(t.toString());
                    //team.removeTask(team.getNextTask().getName(),team.getNextTask().getDuration());

                    //System.out.println(current.toString());
                    //team.removeTask(team.getNextTask().getName(),team.getNextTask().getDuration());

                } else if (tk.status() == TaskStatus.TESTING) {
                    tk.test();
                }
            }
        }



        System.out.printf("%s: Ha realitzat totes les tasques assignades.\n", this.getName());
    }
}