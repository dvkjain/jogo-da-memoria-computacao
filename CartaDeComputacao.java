import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
            case "nlp":
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

    public static CartaDeComputacao[] selecionarCartasParaJogo(CartaDeComputacao[] todas) {
        return selecionar(todas, 8);
    }

    // Overload do método selecionarCartasParaJogo
    public static CartaDeComputacao[] selecionarCartasParaJogo(CartaDeComputacao[] todas, int qtdePares) {
        int pares = qtdePares;
        return selecionar(todas, pares);
    }

    private static CartaDeComputacao[] selecionar(CartaDeComputacao[] todas, int pares) {
        CartaDeComputacao[] listaDePares = new CartaDeComputacao[pares*2];
        int[] indicesEscolhidos = new int[pares*2];

        // Preencher com -1
        for(int k=0; k < indicesEscolhidos.length; k++) {
                    indicesEscolhidos[k] = -1;
        }
        
        Random gerador = new Random();
        int idx;

        for (int i = 0; i<pares*2; i+=2) {

            do {
            idx = gerador.nextInt(todas.length);
            } while (jaEscolhido(indicesEscolhidos, idx));

            CartaDeComputacao parAtual = acharPar(todas, todas[idx]);

            listaDePares[i] = todas[idx];
            listaDePares[i+1] = parAtual;
            indicesEscolhidos[i] = idx;
            indicesEscolhidos[i+1] = acharIdxPar(todas, parAtual);
        }
        return listaDePares;
    }

    private static CartaDeComputacao acharPar(CartaDeComputacao[] cartas, CartaDeComputacao carta) {
        for (int i =0; i<cartas.length; i++) {
            if (cartas[i].getPar().equals(carta.getPar()) && cartas[i]!=carta) return cartas[i];
        }
        return null;
    }

    private static int acharIdxPar(CartaDeComputacao[] cartas, CartaDeComputacao cartaPar) {
        for (int i = 0; i<cartas.length; i++) {
            if (cartas[i] == cartaPar) return i;
        }
        return -1;
    }

    private static boolean jaEscolhido(int[] lista, int valor) {
        for (int i = 0; i<lista.length; i++) {
            if (valor==lista[i]) return true;
        }
        return false;
    }
}