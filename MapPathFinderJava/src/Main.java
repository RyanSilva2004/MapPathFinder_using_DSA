public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
class Graph
{

}
class City
{
    String city_id;
    String city_name;
}
class Path
{
    String path_id;
    City city1;
    City city2;
    double distance;
    public Path(City city1,City city2, double distance)
    {
        this.distance=distance;
    }

}
