package ud2.exams.sushi;

public class Sushi {
    public static void main(String[] args) throws RuntimeException {
        SushiRestaurant restaurant = new SushiRestaurant(10);
        Waiter w1 = new Waiter(5, restaurant);
        Waiter w2 = new Waiter(7, restaurant);

        restaurant.start();
     try {
         //if (currentSushiPieces =>5)
         //Queria poner un contador de piezas de shusi para
         //que a partir de 5 las llenaran
            w1.start();
            w2.start();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {

            w1.join();
            w2.join();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Se han servido todos los platos");
        restaurant.interrupt();
    }
}