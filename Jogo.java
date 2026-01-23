import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Jogo extends JFrame implements ActionListener {

    private JPanel painelCartas;
    private JLabel labelPontuacao;
    private JButton jogarNovamenteButton;
    
    private JButton[] botoes;

    // O deck completo (todas as cartas possíveis)
    private CartaDeComputacao[] cartasIniciais;
    // As cartas selecionadas para a partida atual
    private CartaDeComputacao[] cartasEmJogo;
    
    private JButton primeiroBotaoClicado;
    private JButton segundoBotaoClicado;
    private int primeiroIndice, segundoIndice;
    
    private boolean bloqueado = false;
    private int paresEncontrados = 0;
    private int pontuacaoTotal = 0;
    
    private javax.swing.Timer timerErro; 

    public Jogo(CartaDeComputacao[] cartasIniciais) {
        super("Jogo da Memória da Computação");

        this.cartasIniciais = cartasIniciais;

        // Embaralhar as cartas que forem selecionadas
        this.cartasEmJogo = CartaDeComputacao.embaralharCartas(CartaDeComputacao.selecionarCartasParaJogo(cartasIniciais));
        
        configurarJanela();
        inicializarComponentes();
        criarBotoes();
        
        setVisible(true);
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

        jogarNovamenteButton = new JButton("Resetar Jogo");
        jogarNovamenteButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        jogarNovamenteButton.setBackground(new Color(220, 53, 69)); 
        jogarNovamenteButton.setForeground(Color.WHITE);
        jogarNovamenteButton.setFocusPainted(false);
        jogarNovamenteButton.addActionListener(this); 
        painelSuperior.add(jogarNovamenteButton);

        add(painelSuperior, BorderLayout.NORTH);

        painelCartas = new JPanel();
        // O Layout será definido dinamicamente em criarBotoes()
        painelCartas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(painelCartas, BorderLayout.CENTER);
    }
    
    private void criarBotoes() {
        painelCartas.removeAll(); // Limpa botões antigos
        
        // Ajusta o Grid Layout baseado na quantidade de cartas
        int linhas = (int) Math.ceil(cartasEmJogo.length / 4.0);
        painelCartas.setLayout(new GridLayout(linhas, 4, 10, 10));

        botoes = new JButton[cartasEmJogo.length];

        for (int i = 0; i < cartasEmJogo.length; i++) {
            JButton btn = new JButton("?");
            btn.setFont(new Font("SansSerif", Font.BOLD, 24));
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setFocusPainted(false);
            btn.addActionListener(this); 
            botoes[i] = btn;
            painelCartas.add(btn);
        }
        
        // Força a atualização visual da tela
        painelCartas.revalidate();
        painelCartas.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jogarNovamenteButton) {
            // Pergunta a quantidade e reinicia
            solicitarNumeroDePares(); 
            return;
        }

        // Verifica cliques nas cartas
        for (int i = 0; i < botoes.length; i++) {
            if (e.getSource() == botoes[i]) {
                clicarCarta(i);
                return;
            }
        }
    }

    private void solicitarNumeroDePares() {
        int maxParesPossiveis = cartasIniciais.length / 2;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            String input = JOptionPane.showInputDialog(this, 
                    "Quantos pares você quer jogar?\n(Máximo disponível: " + maxParesPossiveis + ")", 
                    "Configurar Novo Jogo", 
                    JOptionPane.QUESTION_MESSAGE);

            // Se o usuário clicar em "Cancelar" ou fechar a janela, cancela
            if (input == null) {
                return; 
            }

            try {
                int qtde = Integer.parseInt(input);

                if (qtde < 1 || qtde > maxParesPossiveis) {
                    // Popup de Aviso (Valor Inválido)
                    JOptionPane.showMessageDialog(this, 
                            "Valor inválido! Por favor digite um número entre 1 e " + maxParesPossiveis + ".",
                            "Atenção", 
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    entradaValida = true;
                    reiniciarJogo(qtde);
                }

            } catch (NumberFormatException ex) {
                // Popup de Aviso (Não é número)
                JOptionPane.showMessageDialog(this, 
                        "Isso não é um número válido.",
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reiniciarJogo(int qtdePares) {
        // Para o timer se estiver rodando para evitar erros visuais
        if (timerErro != null && timerErro.isRunning()) {
            timerErro.stop();
        }
        timerErro = null;

        // Reseta variáveis de controle
        paresEncontrados = 0;
        pontuacaoTotal = 0;
        primeiroBotaoClicado = null;
        segundoBotaoClicado = null;
        bloqueado = false;
        
        labelPontuacao.setText("Pontos: 0");

        // Seleciona novas cartas do deck principal e reconstrói a tela
        
        this.cartasEmJogo = CartaDeComputacao.embaralharCartas(CartaDeComputacao.selecionarCartasParaJogo(cartasIniciais, qtdePares));
        criarBotoes(); // Recria os botões no painel
    }

    private void clicarCarta(int indice) {
        if (bloqueado || botoes[indice] == primeiroBotaoClicado || !botoes[indice].isEnabled()) {
            return;
        }

        JButton btnClicado = botoes[indice];
        btnClicado.setText(cartasEmJogo[indice].getNome());
        btnClicado.setBackground(cartasEmJogo[indice].getCorDoTema());

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
        if (cartasEmJogo[primeiroIndice].igual(cartasEmJogo[segundoIndice])) {
            acertouPar();
        } else {
            errouPar();
        }
    }

    private void acertouPar() {
        Color corSucesso = new Color(34, 139, 34);
        primeiroBotaoClicado.setBackground(corSucesso);
        segundoBotaoClicado.setBackground(corSucesso);
        
        // Desabilitar botões de pares já encontrados
        primeiroBotaoClicado.setEnabled(false);
        segundoBotaoClicado.setEnabled(false);
        
        paresEncontrados++;
        
        int pontosGanhos = cartasEmJogo[primeiroIndice].calcularPontos();
        pontuacaoTotal += pontosGanhos;
        
        labelPontuacao.setText("Pontos: " + pontuacaoTotal);
        
        resetarJogada();
        
        // Verifica Vitória
        if (paresEncontrados == cartasEmJogo.length / 2) {
             int escolha = JOptionPane.showConfirmDialog(this, 
                "Parabéns! Você venceu!\nPontuação Final: " + pontuacaoTotal + "\nJogar de novo?", 
                "Vitória!", 
                JOptionPane.YES_NO_OPTION);
                
            if (escolha == JOptionPane.YES_OPTION) {
                solicitarNumeroDePares(); // Chama a telinha de configuração
            }
        }
    }

    private void errouPar() {
        timerErro = new javax.swing.Timer(1500, evt -> {
            // Verifica se os botões ainda existem (caso o jogo tenha sido resetado no meio do timer)
            if (primeiroBotaoClicado != null && segundoBotaoClicado != null) {
                primeiroBotaoClicado.setText("?");
                primeiroBotaoClicado.setBackground(Color.LIGHT_GRAY);
                segundoBotaoClicado.setText("?");
                segundoBotaoClicado.setBackground(Color.LIGHT_GRAY);
            }
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

            new CartaDeComputacao("Ruby", "Ruby", 1, "Linguagem"),
            new CartaDeComputacao("Ruby", "Ruby", 1, "Linguagem"),

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
            Jogo jogo = new Jogo(deck);
    }
}