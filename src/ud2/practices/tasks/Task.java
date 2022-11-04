package ud2.practices.tasks;

public class Task {
    private int duration;
    private String name;
    boolean finished;

    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
        finished = false;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void work(){
        Thread current = Thread.currentThread();//Del hilo en ejecucion
        System.out.printf("%s: Starting task %s...\n", current.getName(), this.name);

        // TODO: Do the task (sleep DURATION miliseconds)

        try {
            current.sleep(duration);//Duerme el hilo en ejecución dutante el tiempo especificado

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setFinished(true);
        System.out.printf("%s: Finished task %s (%d).\n", current.getName(), this.name, this.duration);
    }
}
