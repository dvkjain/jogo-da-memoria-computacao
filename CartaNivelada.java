public class CartaNivelada extends Carta {
    
    private int nivel;

    public CartaNivelada(String nome, String par, int nivel) {
        super(nome, par);
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
    
    // Nível 1 = 10pts, Nível 2 = 20pts
    public int calcularPontos() {
        return this.nivel * 10;
    }
    
    @Override
    public String toString() {
        return super.toString() + " [Nível " + nivel + "]";
    }
}