package quiz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizApp extends JFrame implements ActionListener {
    Question[] questions;
    int index = 0, score = 0;
    int time = 10;
    Timer timer;
    JLabel questionLabel, timerLabel;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup bg = new ButtonGroup();
    JButton nextBtn;
    String selectedAnswer = "";
    ArrayList<String> summary = new ArrayList<>();

    public QuizApp() {
        List<Question> questionList = Arrays.asList(
            new Question("Capital of India?", new String[] { "Delhi", "Mumbai", "Kolkata", "Chennai" }, "Delhi"),
            new Question("5 + 7 = ?", new String[] { "10", "11", "12", "13" }, "12"),
            new Question("Largest ocean?", new String[] { "Atlantic", "Arctic", "Indian", "Pacific" }, "Pacific"),
            new Question("Fastest land animal?", new String[] { "Lion", "Cheetah", "Tiger", "Leopard" }, "Cheetah"),
            new Question("Which planet is known as the Red Planet?", new String[] { "Earth", "Venus", "Mars", "Jupiter" }, "Mars")
        );

        Collections.shuffle(questionList);
        questions = questionList.toArray(new Question[0]);

        setTitle("Quiz Application with Timer");
        setSize(500, 300);
        setLayout(new GridLayout(6, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        timerLabel = new JLabel("Time left: 10s", SwingConstants.RIGHT);
        add(timerLabel);

        questionLabel = new JLabel();
        add(questionLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            bg.add(options[i]);
            add(options[i]);
            options[i].addActionListener(this);
        }

        nextBtn = new JButton("Next");
        add(nextBtn);
        nextBtn.addActionListener(e -> submitAnswer());

        loadQuestion();
        startTimer();
        setVisible(true);
    }

    void loadQuestion() {
        if (index < questions.length) {
            Question q = questions[index];
            questionLabel.setText("Q" + (index + 1) + ": " + q.question);
            for (int i = 0; i < 4; i++) {
                options[i].setText(q.options[i]);
            }
            bg.clearSelection();
            selectedAnswer = "";
            time = 10;
        } else {
            showResult();
        }
    }

    void startTimer() {
        timer = new Timer(1000, e -> {
            time--;
            timerLabel.setText("Time left: " + time + "s");
            if (time == 0) {
                timer.stop();
                submitAnswer();
            }
        });
        timer.start();
    }

    void submitAnswer() {
        timer.stop();
        Question q = questions[index];
        String result = "Q" + (index + 1) + ": " + q.question + "\nYour Answer: " + 
                        (selectedAnswer.isEmpty() ? "No Answer" : selectedAnswer) + 
                        " | Correct Answer: " + q.correctAnswer;

        if (selectedAnswer.equals(q.correctAnswer)) {
            score++;
            result += " ✅";
        } else {
            result += " ❌";
        }
        summary.add(result);
        index++;
        loadQuestion();
        if (index < questions.length) {
            startTimer();
        }
    }

    public void actionPerformed(ActionEvent e) {
        selectedAnswer = ((JRadioButton) e.getSource()).getText();
    }

    void showResult() {
        StringBuilder result = new StringBuilder("Quiz Finished!\n\nScore: " + score + "/" + questions.length + "\n\nSummary:\n");
        for (String s : summary) {
            result.append(s).append("\n\n");
        }
        JTextArea textArea = new JTextArea(result.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}
