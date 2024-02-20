import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of cities:");
        int numCities = scanner.nextInt();
        LocationGraph graph = new LocationGraph(numCities);

        while (true) {
            System.out.println("Enter 1 to add a city, 2 to add a path, 3 to display the graph, or 4 to exit:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over
            if (choice == 1) {
                System.out.println("Enter the city ID:");
                String cityId = scanner.nextLine();
                System.out.println("Enter the city name:");
                String cityName = scanner.nextLine();
                City city = new City(cityId, cityName);
                graph.addCity(city);
            } else if (choice == 2) {
                System.out.println("Enter the first city ID:");
                String cityId1 = scanner.nextLine();
                System.out.println("Enter the second city ID:");
                String cityId2 = scanner.nextLine();
                System.out.println("Enter the distance:");
                double distance = scanner.nextDouble();
                scanner.nextLine(); // consume newline left-over
                System.out.println("Is the path available? (yes/no)");
                String isAvailableStr = scanner.nextLine();
                boolean isAvailable = isAvailableStr.equalsIgnoreCase("yes");
                Path path = new Path(new City(cityId1, ""), new City(cityId2, ""), distance, isAvailable);
                graph.addPath(path);
            } else if (choice == 3) {
                graph.display();
            } else if (choice == 4) {
                break;
            }
        }

        scanner.close();
    }
}

//Map For Locations implemented using a Graph
class LocationGraph {
    double[][] adjacencyMatrix;
    ArrayList<City> cities;

    public LocationGraph(int numCities) {
        adjacencyMatrix = new double[numCities][numCities];
        cities = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i == j) {
                    adjacencyMatrix[i][j] = 0;
                } else {
                    adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }
    public void removeCity(String cityId) {
        int index = findCityIndex(cityId);
        if (index != -1) {
            // Create new adjacency matrix and cities list
            double[][] newAdjacencyMatrix = new double[cities.size()-1][cities.size()-1];
            ArrayList<City> newCities = new ArrayList<>();

            // Copy over cities and distances, skipping the removed city
            int newIndex = 0;
            for (int i = 0; i < cities.size(); i++) {
                if (i == index) continue;  // Skip the removed city
                newCities.add(cities.get(i));
                int newColumnIndex = 0;
                for (int j = 0; j < cities.size(); j++) {
                    if (j == index) continue;  // Skip the removed city
                    newAdjacencyMatrix[newIndex][newColumnIndex] = adjacencyMatrix[i][j];
                    newColumnIndex++;
                }
                newIndex++;
            }

            // Replace old adjacency matrix and cities list with new ones
            adjacencyMatrix = newAdjacencyMatrix;
            cities = newCities;
        }
    }


    public void addCity(City city) {
        cities.add(city);
    }

    public void addPath(Path path) {
        int index1 = findCityIndex(path.city1.city_id);
        int index2 = findCityIndex(path.city2.city_id);
        if (index1 != -1 && index2 != -1) {
            adjacencyMatrix[index1][index2] = path.path_distance;
            adjacencyMatrix[index2][index1] = path.path_distance;
        }
    }

    private int findCityIndex(String cityId) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).city_id.equals(cityId)) {
                return i;
            }
        }
        return -1;
    }

    public void display() {
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}

class City {
    String city_id;
    String city_name;

    public City(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
    }
}

class Path {
    String path_id;
    City city1;
    City city2;
    double path_distance;
    boolean path_isAvailable;

    public Path(City city1, City city2, double distance, boolean isAvailable) {
        this.city1 = city1;
        this.city2 = city2;
        this.path_distance = distance;
        this.path_isAvailable = isAvailable;
    }

    //Sets Path Availability
    public void blockPath() {
        this.path_isAvailable = false;
    }

    public void unblockPath() {
        this.path_isAvailable = true;
    }
}
