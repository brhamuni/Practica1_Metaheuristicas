public class Pair implements Comparable<Pair>{
    private Double suma;
    private Integer ciudad ;

    Pair(Double suma, Integer ciudad) {
        this.suma = suma;
        this.ciudad = ciudad;
    }

    @Override
    public int compareTo(Pair o) {
        return Double.compare(this.suma, o.suma);
    }
}