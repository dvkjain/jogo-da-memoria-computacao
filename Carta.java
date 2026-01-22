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
    
    // --- POLIMORFISMO DE SOBRECARGA (Overloading) ---
    // Método 1: Compara com outra Carta
    public boolean igual(Carta outra) {
        return this.par.equals(outra.getPar());
    }

    // Método 2: Compara diretamente com a String (ID do par)
    public boolean igual(String parDoOutro) {
        return this.par.equals(parDoOutro);
    }
    
    // --- POLIMORFISMO DE SOBRESCRITA (Overriding) ---
    @Override
    public String toString() {
        return "Carta: " + nome;
    }
}