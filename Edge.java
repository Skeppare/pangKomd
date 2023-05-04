import java.util.Objects;

public class Edge<T> {
    private final T destination;
    private final String name;
    private int weight;

    public Edge(T destination, String name, int weight) {
        this.destination = destination;
        this.name = name;
        this.weight = weight;
    }

    public T getDestination(){
        return this.destination;
    }
    public int getWeight(){
        return weight;
    }
    public void setWeight(int newWeight){
        if(newWeight < 0){
            throw new IllegalArgumentException("Must be greater than zero!");
        }else{
            this.weight = newWeight;
        }
    }
    public String getName(){
        return this.name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("till ");
        sb.append(this.destination);
        sb.append(" med ");
        sb.append(name);
        sb.append(" tar ");
        sb.append(weight);
        return sb.toString();
    }

}
