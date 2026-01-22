public class Carta {
    
    private String nome;
    private String par;

    public Carta(String nome, String par) {
        this.nome = nome;
        this.par = par;
    }

    public String getNome() {
        return nome;
    }

    public String getPar() {
        return par;
    }
    
    public boolean igual(Carta outra) {
        return this.par.equals(outra.getPar());
    }

    public int calcularPontos() {
        return 10;
    }
}