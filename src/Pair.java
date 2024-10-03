class Pair implements Comparable<Pair> {
    private final Integer ciudad;
    private final Double suma;

    Pair( Integer ciudad, Double suma ){
        this.suma = suma;
        this.ciudad = ciudad;
    }

    @Override
    public int compareTo( Pair p ){
        if( this.suma - p.suma <= 0){ return -1; }
        return 1;
    }

    public Integer getCiudad() { return ciudad; }
    public Double getSuma() { return suma; }


}