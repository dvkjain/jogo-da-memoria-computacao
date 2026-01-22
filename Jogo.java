import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Jogo extends JFrame implements ActionListener {

    private JPanel painelCartas;
    private JLabel labelPontuacao;
    private JButton jogarNovamenteButton;
    
    private JButton[] botoes;
    private CartaDeComputacao[] cartas;
    
    private JButton primeiroBotaoClicado;
    private JButton segundoBotaoClicado;
    private int primeiroIndice, segundoIndice;
    private boolean bloqueado = false;
    private int paresEncontrados = 0;
    private int pontuacaoTotal = 0;
    
    private javax.swing.Timer timerErro; 

    public Jogo(CartaDeComputacao[] cartasIniciais) throws IllegalArgumentException {
        super("Jogo da Memória da Computação");

        this.cartas = cartasIniciais;
        embaralharCartas();
        
        configurarJanela();
        inicializarComponentes();
        criarBotoes();
        
        setVisible(true);
    }

    private void embaralharCartas() {
        List<CartaDeComputacao> lista = Arrays.asList(cartas);
        Collections.shuffle(lista);
        cartas = lista.toArray(new CartaDeComputacao[0]);
    }

    private void configurarJanela() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelSuperior.setBackground(Color.WHITE);

        labelPontuacao = new JLabel("Pontos: 0");
        labelPontuacao.setFont(new Font("SansSerif", Font.BOLD, 20));
        painelSuperior.add(labelPontuacao);

        jogarNovamenteButton = new JButton("Reiniciar Jogo");
        jogarNovamenteButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        jogarNovamenteButton.setBackground(new Color(220, 53, 69)); 
        jogarNovamenteButton.setForeground(Color.WHITE);
        jogarNovamenteButton.setFocusPainted(false);
        jogarNovamenteButton.addActionListener(this); 
        painelSuperior.add(jogarNovamenteButton);

        add(painelSuperior, BorderLayout.NORTH);

        painelCartas = new JPanel();
        painelCartas.setLayout(new GridLayout(0, 4, 10, 10));
        painelCartas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(painelCartas, BorderLayout.CENTER);
    }
    
    private void criarBotoes() {
        painelCartas.removeAll();
        botoes = new JButton[cartas.length];

        for (int i = 0; i < cartas.length; i++) {
            JButton btn = new JButton("?");
            btn.setFont(new Font("SansSerif", Font.BOLD, 24));
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setFocusPainted(false);
            btn.addActionListener(this); 
            botoes[i] = btn;
            painelCartas.add(btn);
        }
        painelCartas.revalidate();
        painelCartas.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jogarNovamenteButton) {
            if (JOptionPane.showConfirmDialog(this, "Deseja reiniciar?", "Reiniciar", 
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                reiniciarJogo();
            }
            return;
        }

        for (int i = 0; i < botoes.length; i++) {
            if (e.getSource() == botoes[i]) {
                clicarCarta(i);
                return;
            }
        }
    }

    private void reiniciarJogo() {
        try {
            if (timerErro == null){
            throw new NullPointerException();
            }
            timerErro.stop();
            
        } catch (NullPointerException e) {
            timerErro = null; // redundante, pois se NullPointerException acontecer, timerErro tem que ser null
        } finally {
            timerErro = null;
        }

        paresEncontrados = 0;
        pontuacaoTotal = 0;
        primeiroBotaoClicado = null;
        segundoBotaoClicado = null;
        bloqueado = false;
        
        labelPontuacao.setText("Pontos: 0");

        embaralharCartas();

        for (JButton btn : botoes) {
            btn.setText("?");
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setEnabled(true);
        }
    }

    private void clicarCarta(int indice) {
        if (bloqueado || botoes[indice] == primeiroBotaoClicado || !botoes[indice].isEnabled()) {
            return;
        }

        JButton btnClicado = botoes[indice];
        btnClicado.setText(cartas[indice].getNome());
        btnClicado.setBackground(cartas[indice].getCorDoTema());

        if (primeiroBotaoClicado == null) {
            primeiroBotaoClicado = btnClicado;
            primeiroIndice = indice;
        } else {
            segundoBotaoClicado = btnClicado;
            segundoIndice = indice;
            bloqueado = true;
            verificarPar();
        }
    }

    private void verificarPar() {
        if (cartas[primeiroIndice].igual(cartas[segundoIndice])) {
            acertouPar();
        } else {
            errouPar();
        }
    }

    private void acertouPar() {
        Color corSucesso = new Color(34, 139, 34);
        primeiroBotaoClicado.setBackground(corSucesso);
        segundoBotaoClicado.setBackground(corSucesso);
        
        primeiroBotaoClicado.setEnabled(false);
        segundoBotaoClicado.setEnabled(false);
        
        paresEncontrados++;
        
        int pontosGanhos = cartas[primeiroIndice].calcularPontos();
        pontuacaoTotal += pontosGanhos;
        
        labelPontuacao.setText("Pontos: " + pontuacaoTotal);
        
        resetarJogada();
        
        if (paresEncontrados == cartas.length / 2) {
             int escolha = JOptionPane.showConfirmDialog(this, 
                "Parabéns! Você venceu!\nPontuação Final: " + pontuacaoTotal + "\nJogar de novo?", 
                "Vitória!", 
                JOptionPane.YES_NO_OPTION);
                
            if (escolha == JOptionPane.YES_OPTION) {
                reiniciarJogo();
            }
        }
    }

    private void errouPar() {
        timerErro = new javax.swing.Timer(1500, evt -> {
            primeiroBotaoClicado.setText("?");
            primeiroBotaoClicado.setBackground(Color.LIGHT_GRAY);
            segundoBotaoClicado.setText("?");
            segundoBotaoClicado.setBackground(Color.LIGHT_GRAY);
            resetarJogada();
        });
        timerErro.setRepeats(false); // timer deve ser executado apenas uma vez, e depois parar
        timerErro.start();
    }

    private void resetarJogada() {
        primeiroBotaoClicado = null;
        segundoBotaoClicado = null;
        bloqueado = false;
    }

    public static void main(String[] args) {
        CartaDeComputacao[] deck = {
            new CartaDeComputacao("Java", "Java", 1, "Linguagem"),
            new CartaDeComputacao("Java", "Java", 1, "Linguagem"),

            new CartaDeComputacao("Python", "Python", 1, "Linguagem"),
            new CartaDeComputacao("Python", "Python", 1, "Linguagem"),

            new CartaDeComputacao("C++", "C++", 1, "Linguagem"),
            new CartaDeComputacao("C++", "C++", 1, "Linguagem"),

            new CartaDeComputacao("JS", "JS", 1, "Linguagem"),
            new CartaDeComputacao("JS", "JS", 1, "Linguagem"),

            new CartaDeComputacao("Mimi", "MimiGato", 1, "Outros"),
            new CartaDeComputacao("Gato", "MimiGato", 1, "Outros"),

            new CartaDeComputacao("Hello", "HelloWorld", 1, "Outros"),
            new CartaDeComputacao("World", "HelloWorld", 1, "Outros"),

            new CartaDeComputacao("Totó", "TotóCachorro", 1, "Outros"),
            new CartaDeComputacao("Cachorro", "TotóCachorro", 1, "Outros"),

            new CartaDeComputacao("Turing", "TuringEnigma", 2, "História"),
            new CartaDeComputacao("Enigma", "TuringEnigma", 2, "História"),

            new CartaDeComputacao("Ada", "AdaLovelace", 2, "História"),
            new CartaDeComputacao("Lovelace", "AdaLovelace", 2, "História"),

            new CartaDeComputacao("Binário", "Binário01", 2, "Fundamentos"),
            new CartaDeComputacao("0101", "Binário01", 2, "Fundamentos"),

            new CartaDeComputacao("while(true)", "whileEterno", 2, "Fundamentos"),
            new CartaDeComputacao("Eternidade", "whileEterno", 2, "Fundamentos"),

            new CartaDeComputacao("Linux", "LinuxPinguim", 1, "SO"),
            new CartaDeComputacao("Pinguim", "LinuxPinguim", 1, "SO"),

            new CartaDeComputacao("Windows", "WindowsJanelas", 1, "SO"),
            new CartaDeComputacao("Janelas", "WindowsJanelas", 1, "SO"),

            new CartaDeComputacao("Word2Vec", "Vetorização", 2, "NLP"),
            new CartaDeComputacao("GloVe", "Vetorização", 2, "NLP"),

            new CartaDeComputacao("Eliza", "Chatbot", 2, "NLP"),
            new CartaDeComputacao("ChatGPT", "Chatbot", 2, "NLP"),


        };
            CartaDeComputacao[] deckJogo = CartaDeComputacao.selecionarCartasParaJogo(deck);
            Jogo jogo = new Jogo(deckJogo);
    }
}
