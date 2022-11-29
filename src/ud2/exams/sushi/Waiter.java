package ud2.exams.sushi;

public class Waiter extends Thread {
    private SushiRestaurant restaurant;
    private int dishes;
    private int currentDishes;

    public Waiter(int dishes, SushiRestaurant restaurant) {
        super();
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.currentDishes = 0;
    }

    @Override
    public void run() {
        while(currentDishes < dishes){
            restaurant.retrieveShushiDish();
            currentDishes++;
            System.out.printf("Waiter has retrieved dish. (%d/%d)\n", currentDishes, dishes);
        }
    }
}
