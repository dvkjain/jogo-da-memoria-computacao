import java.awt.Color;

public class CartaDeComputacao extends CartaNivelada {
    
    private String tema;

    public CartaDeComputacao(String nome, String par, int nivel, String tema) {
        super(nome, par, nivel);
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }
    
    // Definir a cor do botão baseado no tema
    public Color getCorDoTema() {
        switch (tema.toLowerCase()) {
            case "linguagem":
                return new Color(255, 182, 193); // Rosa Claro
            case "web":
                return new Color(173, 216, 230); // Azul Claro
            case "dados":
            case "banco de dados":
                return new Color(144, 238, 144); // Verde Claro
            case "devops":
                return new Color(255, 255, 224); // Amarelo Claro
            case "outros":
                return new Color(221, 160, 221); // Roxo Claro
            default:
                return Color.WHITE;
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Tema: " + tema;
    }
}