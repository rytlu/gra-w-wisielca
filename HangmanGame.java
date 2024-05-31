import javax.swing.*; 
import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.Random; 

public class HangmanGame extends JFrame { 
    private static List<String> wordList = new ArrayList<>(); 
    private String currentWord; 
    private char[] guessedWord; 
    private int attemptsLeft; 
    private int maxAttempts; 
    private int wins = 0; 
    private int losses = 0; 
    private int attempts = 0; 
    private JLabel wordLabel; 
    private JLabel attemptsLabel; 
    private JLabel messageLabel; 
    private JTextField guessField; 
    private JTextArea guessedLettersArea; 
    private JLabel winsLabel; 
    private JLabel lossesLabel; 
    private JLabel attemptsLabelStats; 
    private List<Character> guessedLetters; 

    public HangmanGame() { 
        initializeWordList(); 
        setupGUI(); 
        newGame(10); 
    }

    private void initializeWordList() { 
        wordList.add("kot"); 
        wordList.add("pies"); 
        wordList.add("samochód"); 
        wordList.add("programowanie"); 
        wordList.add("java"); 
        wordList.add("komputer"); 
        wordList.add("rower"); 
        wordList.add("telefon"); 
        wordList.add("konsola"); 
        wordList.add("ekran"); 
        wordList.add("lekkoatletyka");
        wordList.add("interpunkcja");
        wordList.add("telekomunikacja");
        wordList.add("metamorfoza");
        wordList.add("prześladowanie");
        wordList.add("antyterrorysta");
        wordList.add("dźwiękonaśladownictwo");
        wordList.add("antykoncepcja");
        wordList.add("kolorowanka");
        wordList.add("konstantynopolitański");
        wordList.add("absolwent");
        wordList.add("gżegżółka");
        wordList.add("kolorowy");
        wordList.add("cukiernia");
        wordList.add("elektryka");
        wordList.add("gitarzysta");
        wordList.add("helikopter");
        wordList.add("informatyk");
        wordList.add("kalendarz");
        wordList.add("kardiolog");   
    }

    private void setupGUI() { 
        setTitle("Gra w wisielca"); 
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout()); 

        JPanel topPanel = new JPanel(); 
        wordLabel = new JLabel(); 
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24)); 
        topPanel.add(wordLabel); 
        
        JPanel leftPanel = new JPanel(); 
        leftPanel.setLayout(new GridLayout(5, 1)); 
        guessField = new JTextField(2); 
        attemptsLabel = new JLabel("Pozostało prób: "); 
        messageLabel = new JLabel("Zgadnij literę:"); 
        JButton guessButton = new JButton("Zgadnij"); 
        JPanel guessPanel = new JPanel(); 
        guessPanel.add(guessField); 
        guessPanel.add(guessButton);
        leftPanel.add(attemptsLabel); 
        leftPanel.add(messageLabel); 
        leftPanel.add(guessPanel); 
        
        JPanel rightPanel = new JPanel(); 
        rightPanel.setLayout(new GridLayout(2, 1, 100, 100)); 
        winsLabel = new JLabel("Wygrane: 0"); 
        lossesLabel = new JLabel("Przegrane: 0"); 
        attemptsLabelStats = new JLabel("Próby: 0"); 
        JPanel statsPanel = new JPanel();
        statsPanel.add(winsLabel);
        statsPanel.add(lossesLabel);
        statsPanel.add(attemptsLabelStats);
        rightPanel.add(statsPanel);

        JPanel middlePanel = new JPanel(); 
        middlePanel.setLayout(new FlowLayout(5,150,250));
        guessedLettersArea = new JTextArea(); 
        guessedLettersArea.setEditable(false); 
        guessedLettersArea.setPreferredSize(new Dimension(300, 25));
        middlePanel.add(guessedLettersArea); 

        JPanel bottomPanel = new JPanel();
        JButton newGameButton = new JButton("Nowa Gra"); 
        JButton manageWordsButton = new JButton("Edytuj słowa");
        bottomPanel.add(newGameButton); 
        bottomPanel.add(manageWordsButton); 

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel,BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
       
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                guessLetter(); 
            }
        });

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectDifficulty(); 
            }
        });

        manageWordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                manageWordList(); 
            }
        });
    }

    private void selectDifficulty() {
        String[] options = {"Łatwy", "Średni", "Trudny"}; 
        int choice = JOptionPane.showOptionDialog(this, "Wybierz poziom trudności", "Nowa Gra",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); 

        switch (choice) {
            case 0: maxAttempts = 10; break; 
            case 1: maxAttempts = 7; break;  
            case 2: maxAttempts = 5; break;  
            default: maxAttempts = 10; break; 
        }

        newGame(maxAttempts);
    }

    private void newGame(int maxAttempts) {
        this.maxAttempts = maxAttempts; 
        this.attemptsLeft = maxAttempts; 
        this.guessedLetters = new ArrayList<>(); 
        this.currentWord = getRandomWord(); 
        this.guessedWord = new char[currentWord.length()]; 
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_'; 
        }
        updateGUI(); 
    }

    private String getRandomWord() {
        Random random = new Random(); 
        return wordList.get(random.nextInt(wordList.size())); 
    }

    private void guessLetter() {
        String input = guessField.getText(); 
        if (input.length() != 1) {
            messageLabel.setText("Podaj dokładnie jedną literę."); 
            return;
        }
        char guess = input.charAt(0); 
        if (guessedLetters.contains(guess)) {
            messageLabel.setText("Ta litera została już zgadnięta."); 
            return;
        }
        guessedLetters.add(guess); 
        boolean correctGuess = false; 

        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == guess) {
                guessedWord[i] = guess; 
                correctGuess = true; 
            }
        }

        if (!correctGuess) {
            attemptsLeft--; 
        }

        if (String.valueOf(guessedWord).equals(currentWord)) {
            wins++; 
            attempts++; 
            JOptionPane.showMessageDialog(this, "Gratulacje! Zgadłeś słowo: " + currentWord); 
            newGame(maxAttempts); 
        } else if (attemptsLeft == 0) {
            losses++; 
            attempts++; 
            JOptionPane.showMessageDialog(this, "Przegrałeś. Słowo to: " + currentWord); 
            newGame(maxAttempts); 
        }
        updateGUI(); 
    }

    private void updateGUI() {
        wordLabel.setText(String.valueOf(guessedWord)); 
        attemptsLabel.setText("Pozostało prób: " + attemptsLeft); 
        guessedLettersArea.setText("Zgadnięte litery: " + guessedLetters); 
        guessField.setText(""); 
        winsLabel.setText("Wygrane: " + wins); 
        lossesLabel.setText("Przegrane: " + losses); 
        attemptsLabelStats.setText("Próby: " + attempts); 
    }

    private void manageWordList() {
        String[] options = {"Wyświetl słowa", "Dodaj słowo", "Usuń słowo", "Wróć"}; 
        int choice = JOptionPane.showOptionDialog(this, "Zarządzanie bazą słów", "Zarządzaj",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); 

        switch (choice) {
            case 0: displayWords(); break; 
            case 1: addWord(); break; 
            case 2: deleteWord(); break; 
            default: break; 
        }
    }

    private void displayWords() {
        StringBuilder words = new StringBuilder("Baza słów:\n"); 
        for (int i = 0; i < wordList.size(); i++) {
            words.append(i + 1).append(". ").append(wordList.get(i)).append("\n"); 
        }
        JOptionPane.showMessageDialog(this, words.toString()); 
    }

    private void addWord() {
        String newWord = JOptionPane.showInputDialog(this, "Podaj nowe słowo:"); 
        if (newWord != null && !newWord.trim().isEmpty()) {
            wordList.add(newWord.trim()); 
            JOptionPane.showMessageDialog(this, "Słowo zostało dodane."); 
        } else {
            JOptionPane.showMessageDialog(this, "Nieprawidłowe słowo."); 
        }
    }

    private void deleteWord() {
        StringBuilder words = new StringBuilder("Baza słów:\n"); 
        for (int i = 0; i < wordList.size(); i++) {
            words.append(i + 1).append(". ").append(wordList.get(i)).append("\n"); 
        }
        String input = JOptionPane.showInputDialog(this, words.toString() + "\nPodaj numer słowa do usunięcia:"); 
        try {
            int index = Integer.parseInt(input) - 1; 
            if (index >= 0 && index < wordList.size()) {
                wordList.remove(index); 
                JOptionPane.showMessageDialog(this, "Słowo zostało usunięte."); 
            } else {
                JOptionPane.showMessageDialog(this, "Nieprawidłowy numer."); 
            }
        } catch (NumberFormatException event) {
            JOptionPane.showMessageDialog(this, "Nieprawidłowy numer."); 
        }
    }
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(new Runnable() { 
            @Override
            public void run() {
                new HangmanGame().setVisible(true); 
            }
        });
    }
}