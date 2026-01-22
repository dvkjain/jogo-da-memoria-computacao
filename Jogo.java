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
        super("Jogo da Memória - POO");
        
        // Validações com throw (já existiam, cumprem o requisito também)
        if (cartasIniciais == null || cartasIniciais.length == 0) {
            throw new IllegalArgumentException("O baralho está vazio!");
        }
        if (cartasIniciais.length % 2 != 0) {
            throw new IllegalArgumentException("O número de cartas é ímpar (" + cartasIniciais.length + ").");
        }

        this.cartas = cartasIniciais;
        embaralharCartas();
        
        configurarJanela();
        inicializarComponentes();
        criarBotoes();
        
        setVisible(true);
    }

    private void embaralharCartas() {
        List<CartaDeComputacao> lista = Arrays.asList(this.cartas);
        Collections.shuffle(lista);
        this.cartas = lista.toArray(new CartaDeComputacao[0]);
    }

    private void configurarJanela() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
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
        }
        finally {
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
        timerErro.setRepeats(false);
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
            new CartaDeComputacao("JS", "JS", 1, "Web"),
            new CartaDeComputacao("JS", "JS", 1, "Web"),
            new CartaDeComputacao("HTML", "HTML", 1, "Web"),
            new CartaDeComputacao("HTML", "HTML", 1, "Web"),
            new CartaDeComputacao("CSS", "CSS", 1, "Web"),
            new CartaDeComputacao("CSS", "CSS", 1, "Web"),
            new CartaDeComputacao("SQL", "SQL", 1, "Dados"),
            new CartaDeComputacao("SQL", "SQL", 2, "Dados"),
            new CartaDeComputacao("Git", "Git", 2, "DevOps"),
            new CartaDeComputacao("Git", "Git", 2, "DevOps"),
            new CartaDeComputacao("Mimi", "MimiGato", 1, "Outros"),
            new CartaDeComputacao("Gato", "MimiGato", 1, "Outros")
        };

            Jogo jogo = new Jogo(deck);

    }
}