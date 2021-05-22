import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private HazelcastInstance instance;
    final private static Random random = new Random(System.currentTimeMillis());


    public Menu(HazelcastInstance instance) {
        this.instance = instance;
    }

    private void printMenu(){
        System.out.println("Please choose operation:");
        System.out.println("1: Add");
        System.out.println("2: Select by ID");
        System.out.println("3: Select All");
        System.out.println("4: Update release year");
        System.out.println("5: Delete by ID");
        System.out.println("9: Exit");
    }

    public void selectOperation(){
        boolean doExit = false;
        Scanner scanner = new Scanner(System.in);
        while (!doExit){
            printMenu();
            int op = scanner.nextInt();

            switch (op){
                case 1:
                    System.out.println("Adding");
                    addMovie();
                    break;
                case 2:
                    System.out.println("Select by ID");
                    selectById();
                    break;
                case 3:
                    System.out.println("Select All");
                    selectAll();
                    break;
                case 4:
                    System.out.println("Update");
                    updateMovie();
                    break;
                case 5:
                    System.out.println("Delete");
                    deleteById();
                    break;
                case 9:
                    System.out.println("Exit");
                    doExit=true;
                    break;
                default:
                    System.out.println("Not a recognizable choice");
                    break;
            }
        }


    }

    private void addMovie(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie name:");
        String name = scanner.nextLine();
        System.out.println("Movie release year:");
        int year = scanner.nextInt();

        Map<Long, Movie> movies = instance.getMap("movies");
        Long key = (long) Math.abs(random.nextInt());
        Movie movie = new Movie(name, year);
        System.out.println("PUT " + key + " => " + movie);
        movies.put(key, movie);
    }
    private void selectById(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie id:");
        long id = scanner.nextLong();
        Map<Long, Movie> movies = instance.getMap("movies");

        Movie movie = movies.get(id);
        System.out.println(movie);
        //System.out.println("id:"+e.getKey() + " -> " + e.getValue());

    }

    private void selectAll(){

        Map<Long, Movie> movies = instance.getMap("movies");
        System.out.println("All movies: ");
        for(Map.Entry<Long, Movie> e : movies.entrySet()){
            System.out.println("id:"+e.getKey() + " -> " + e.getValue());
        }
    }

    private void updateMovie(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie id:");
        long id = scanner.nextLong();
        Map<Long, Movie> movies = instance.getMap("movies");
        System.out.println("year:");
        int year = scanner.nextInt();

        System.out.println("Before update:" + movies.get(id));
        Movie movie = movies.get(id);
        movie.setYear(year);
        movies.put(id,movie);
        System.out.println("After update:" + movies.get(id));

    }

    private void deleteById(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie id:");
        long id = scanner.nextLong();
        Map<Long, Movie> movies = instance.getMap("movies");

        Movie movie = movies.get(id);
        System.out.println("deleting movie: "+ movie);
        movies.remove(id);

    }


}
