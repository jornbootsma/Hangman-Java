import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    
    static int maxGuesses = 9;
    int wrongGuesses;
    int correctLetters;
    int guessesLeft;
    String word;
    String endMessage;
    boolean gameEnds;
    char[] state;
    List<Character> missedLetters;

    public static String[] wordsToUse = {"aikido", "baseball", "cricket", "darts", "football",
                                        "golf", "handball", "judo", "karate", "lacrosse",
                                        "giraffe", "rhino", "truck", "blue", "potato", "school", "elephant"};
    
    public Hangman() {
        word = randomWord();
        missedLetters = new ArrayList<Character>();
        state = new char[word.length()];
        Arrays.fill(state, '_');
        wrongGuesses = 0;
        correctLetters = 0;
        gameEnds = false;
        endMessage = "";
    }

    /**
     * Returns a random integer number between the given lower bound and upper bound.
     * @param upperBound
     * @param lowerBound
     * @return randomNumber
     */
    public static int randomNumber(int upperBound, int lowerBound) {
        Random r = new Random();
        return r.nextInt(upperBound - lowerBound) + lowerBound;
    }

    /**
     * A random word is picked from the array of possible words.
     * @return randomWord
     */
    public static String randomWord() {
        int rand = randomNumber(wordsToUse.length, 0);
        return wordsToUse[rand];
    }

    /**
     * Runs the hangman game.
     * In each round the user is asked to give a letter as input.
     * It is checked whether or not this letter is correct.
     * The guessed letters are saved, either in an object with
     * correct letters or in an object with missed letters.
     * After each round the current progress is shown to the user.
     */
    public void playGame() {
        System.out.println("Type \"quit\" whenever you want to stop the game.");
        displayProgress();
        while (true) {
            char letter = askLetter();
            if (!gameEnds) {
                if (word.contains(Character.toString(letter))) {
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == letter) {
                            state[i] = letter;
                            correctLetters ++;
                        }
                    }
                } else {
                    missedLetters.add(letter);
                    wrongGuesses ++;
                }
            }

            displayProgress();

            if (word.length() == correctLetters) {
                gameEnds = true;
                endMessage = "Congratulations, you have won the game!";
            }

            if (wrongGuesses >= maxGuesses) {
                gameEnds = true;
                endMessage = "You have lost the game. The correct answer was \"" + word + "\".";
            }

            if (gameEnds) {
                System.out.println(endMessage);
                break;
            }
        }
    }

    /**
     * Asks the user to give some input letter. It checks whether the given
     * input is valid.
     * @return char guess
     */
    public char askLetter() {
        Scanner input = new Scanner(System.in);
        char guess = 'a';
        while (true) {
            String letter = input.next();
            if (letter.equals("quit") || letter.equals("Quit") || letter.equals("QUIT")) {
                gameEnds = true;
                endMessage = "The game is stopped prematuraly.";
                break;
            } else {
                if (letter.length() == 1) {
                    guess = letter.charAt(0);
                    if (Character.isAlphabetic(guess)) {
                        guess = Character.toLowerCase(guess);
                        
                        boolean alreadyTried = alreadyTried(guess);

                        if (alreadyTried) {
                            System.out.println("The given input has already been tried. Try another letter.");
                        } else {
                            break;
                        }

                    } else {
                        System.out.println("The given input is not a letter. Try again.");
                    }
                    
                } else {
                    System.out.println("Wrong input, you need to choose exactly one letter. Try again.");
                }
            }
        }
        
        return guess;
    }

    /**
     * Checks if a given character has already been tried.
     * It first checks if the character is available in the wrong guessed letters.
     * If not, it checks if the character is available in correct guessed letters.
     * @param c
     * @return true if c is already tried
     */
    public boolean alreadyTried(char c) {
        boolean alreadyTried = false;
        if (missedLetters.contains(c)) {
            alreadyTried = true;
        } else {
            for (char c2 : state) {
                if (c2 == c) {
                    alreadyTried = true;
                    break;
                }
            }
        }
        return alreadyTried;
    }

    public void displayProgress() {
        guessesLeft = maxGuesses - wrongGuesses;
        if (guessesLeft > 1) {
            System.out.println("You have " + guessesLeft + " guesses left.");
        } else {
            System.out.println("You have " + guessesLeft + " guess left.");
        }
        System.out.println("Missed letters: " + missedLetters);
        for (char c : state) {
			System.out.print(c + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Hangman hangman = new Hangman();
        hangman.playGame();
    }
}
