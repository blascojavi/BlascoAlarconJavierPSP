package ud2.practices.collectivetasks;

public class Task {
    private int duration;
    private String name;
    private TaskStatus status;

    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
        status = TaskStatus.UNFINISHED;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public TaskStatus status() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public synchronized void work(){
        //traemos la tarea "t"
        Thread current = Thread.currentThread();
        System.out.printf("%s: Starting task %s...\n", current.getName(), this.name);

        // TODO: Do the task (sleep DURATION miliseconds)
        try{
            current.sleep(duration);


        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        //============================


        setStatus(TaskStatus.TESTING);
        System.out.printf("%s: Finished task %s (%d).\n", current.getName(), this.name, this.duration);
    }

    public synchronized void test(){
        Thread current = Thread.currentThread();
        System.out.printf("%s: Starting test %s...\n", current.getName(), this.name);

        // TODO: Do the task (sleep DURATION / 2 miliseconds)
        try{

            current.sleep(duration /2);

        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        setStatus(TaskStatus.FINISHED);
        System.out.printf("%s: Finished test %s (%d).\n", current.getName(), this.name, this.duration/2);
    }
}