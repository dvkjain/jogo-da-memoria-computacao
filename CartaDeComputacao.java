import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
// import java.util.Random;

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
            case "história":
                return new Color(173, 216, 230); // Azul Claro
            case "fundamentos":
                return new Color(144, 238, 144); // Verde Claro
            case "so":
                return new Color(255, 255, 224); // Amarelo Claro
            case "pln":
                return new Color(221, 160, 221); // Roxo Claro
            default:
                return Color.WHITE;
        }
    }

    public static CartaDeComputacao[] embaralharCartas(CartaDeComputacao[] cartas) {
        // Transforma em lista, para fazer .shuffle, e depois transforma de volta em array
        List<CartaDeComputacao> lista = Arrays.asList(cartas);
        Collections.shuffle(lista);
        return lista.toArray(new CartaDeComputacao[0]);
    }

    // Overload do método selecionarCartasParaJogo
    public static CartaDeComputacao[] selecionarCartasParaJogo(CartaDeComputacao[] todas) {
        return selecionarCartasParaJogo(todas, 8);
    }

    public static CartaDeComputacao[] selecionarCartasParaJogo(CartaDeComputacao[] todas, int pares) {
        List<CartaDeComputacao> cartasEmbaralhadas = Arrays.asList(todas);
        Collections.shuffle(cartasEmbaralhadas); // Embaralha as cartas

        List<CartaDeComputacao> cartasSelecionadas = new ArrayList<>();
        List<String> paresUsados = new ArrayList<>();

        for (CartaDeComputacao carta : cartasEmbaralhadas) {
            if (paresUsados.contains(carta.getPar())) continue; // Se a carta ja foi utilizada, continua o loop

            CartaDeComputacao par = acharPar(cartasEmbaralhadas, carta);
            
            cartasSelecionadas.add(carta);
            cartasSelecionadas.add(par);
            paresUsados.add(carta.getPar());

            if (paresUsados.size() == pares) break; // Se já chegou na quantidade de pares, encerra o loop
        }

        Collections.shuffle(cartasSelecionadas); // Embaralha as cartas selecionadas
        return cartasSelecionadas.toArray(new CartaDeComputacao[0]);
    }

    // Função auxiliar para achar o par da carta
    private static CartaDeComputacao acharPar(List<CartaDeComputacao> cartasEmbaralhadas, CartaDeComputacao carta) {
        for (CartaDeComputacao c : cartasEmbaralhadas) {
            if (c != carta && (c.getPar().equals(carta.getPar()))) {
                return c;
            }
        }
        return null;
    }
}