package wordguesser.service;

import java.util.ArrayList;
import java.util.List;
import wordguesser.model.LetterState;

public class GameEngine {

    public static List<LetterState> evaluateGuess(String guess, String target) {
        guess = guess.toLowerCase();
        target = target.toLowerCase();

        List<LetterState> result = new ArrayList<>();
        int[] targetCounts = new int[26];
        boolean[] matched = new boolean[target.length()];

        for (int i = 0; i < target.length(); i++) {
            targetCounts[target.charAt(i) - 'a']++;
            result.add(LetterState.ABSENT);
        }

        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                result.set(i, LetterState.CORRECT);
                targetCounts[guess.charAt(i) - 'a']--;
                matched[i] = true;
            }
        }

        for (int i = 0; i < guess.length(); i++) {
            if (!matched[i]) {
                int index = guess.charAt(i) - 'a';
                if (index >= 0 && index < 26 && targetCounts[index] > 0) {
                    result.set(i, LetterState.PRESENT);
                    targetCounts[index]--;
                }
            }
        }

        return result;
    }
}