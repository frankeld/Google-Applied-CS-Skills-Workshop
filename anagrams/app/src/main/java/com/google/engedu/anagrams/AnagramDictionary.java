/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private Set<String> wordSet = new HashSet<>();
    private Map<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private Map<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);

            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> wordCombos = new ArrayList<>();
                wordCombos.add(word);
                sizeToWords.put(word.length(), wordCombos);
            }

            String sortedWord = sortLetters(word);
            if (!lettersToWord.containsKey(sortedWord)) {
                lettersToWord.put(sortedWord, new ArrayList<String>());
            }
            lettersToWord.get(sortedWord).add(word);

        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    private List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }

    private String sortLetters(String unsortedString) {
        char[] unsortedLetters = unsortedString.toCharArray();
        Arrays.sort(unsortedLetters);
        return Arrays.toString(unsortedLetters);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<>();
        for (char letter = 'a'; letter <= 'z'; ++letter){
            if (lettersToWord.containsKey(sortLetters(word + letter))) {
                result.addAll(getAnagrams(word + letter));
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        int numbersOfWordsWithSize = sizeToWords.get(wordLength).size();
        int i = random.nextInt(numbersOfWordsWithSize);
        String currentWordOption = sizeToWords.get(wordLength).get(i);
        while (getAnagramsWithOneMoreLetter(currentWordOption).size() < MIN_NUM_ANAGRAMS) {
            ++i;
            if (i == numbersOfWordsWithSize) {
                i = 0;
            }
            currentWordOption = sizeToWords.get(wordLength).get(i);
        }
        if (wordLength != MAX_WORD_LENGTH) {
            wordLength++;
        }
        return currentWordOption;
    }
}
