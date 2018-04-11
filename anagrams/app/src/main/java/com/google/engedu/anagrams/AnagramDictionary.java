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

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;

    private static final char[] ALPHABETS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                                            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                                            'v', 'w', 'x', 'y', 'z'};

    private Random random = new Random();

    private static int wordLength = DEFAULT_WORD_LENGTH;

    private List<String> wordList = new ArrayList<>();
    private Set<String> wordSet = new HashSet<>();
    private Map<String, List<String>> lettersToWord = new HashMap<>();
    private Map<Integer, List<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            String key = sortLetters(word);
            if (!lettersToWord.containsKey(key)) {
                lettersToWord.put(key, new ArrayList<String>());
            }
            lettersToWord.get(key).add(word);

            Integer size = word.length();
            if (!sizeToWords.containsKey(size)) {
                sizeToWords.put(size, new ArrayList<String>());
            }
            sizeToWords.get(size).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !base.contains(word);
    }

    public List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }
    @NonNull
    private static String sortLetters(String string) {
        char[] chars = string.toCharArray();
        Arrays.sort(chars);
        return charArrayToString(chars);
    }

    @NonNull
    private static String charArrayToString(char[] chars) {
        StringBuilder result = new StringBuilder();
        for (char c : chars) {
            result.append(c);
        }
        return result.toString();
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for (char c : ALPHABETS) {
            String key = sortLetters(word + c);
            if (lettersToWord.containsKey(key)) {
                result.addAll(lettersToWord.get(key));
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        List<String> goodStartWords = sizeToWords.get(wordLength);
        String result = goodStartWords.get(random.nextInt(goodStartWords.size()));

        while (getAnagramsWithOneMoreLetter(result).size() <= MIN_NUM_ANAGRAMS) {
            result = goodStartWords.get(random.nextInt(goodStartWords.size()));
        }
        wordLength = (wordLength+1 > MAX_WORD_LENGTH) ? MAX_WORD_LENGTH : wordLength + 1;
        return result;
    }
}
