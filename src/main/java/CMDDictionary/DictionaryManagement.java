package CMDDictionary;

import searchingAlgorithm.Trie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

public class DictionaryManagement {
    private final Dictionary dictionary;
    public Trie trie= new Trie();
    DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of words to add: ");
        int numberOfWords = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numberOfWords; i++) {
            System.out.print("Enter English word: ");
            String wordFound = scanner.nextLine();
            System.out.print("Enter Vietnamese meaning: ");
            String wordExplain = scanner.nextLine();
            Word word = new Word(wordFound, wordExplain);
            System.out.println(word.getWordFound());
            dictionary.words.add(word);
            System.out.println("Word added to the dictionary.");
        }
    }

    public void showAllWords() {
        // Sắp xếp danh sách theo thứ tự alphabet (English word_target)
        Collections.sort(dictionary.words, (word1, word2) -> word1.wordFound.compareToIgnoreCase(word2.wordFound));
        // In danh sách
        System.out.println("No | English | Vietnamese");
        for (int i = 0; i < dictionary.words.size(); i++) {
            Word word = dictionary.words.get(i);
            System.out.println((i + 1) + " | " + word.getWordFound() + " | " + word.getWordExplaination());
        }
    }
//
    public void insertFromFile(String filePath) {
        filePath = "src/main/java/CMDDictionary/"
                + filePath + ".txt";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File do not exits");
            }
            else {
                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split("\t");

                    if (parts.length == 2) {
                        String wordFound = parts[0];
                        String wordExplain = parts[1];

                        // Thêm từ mới vào từ điển
                        Word word = new Word(wordFound, wordExplain);
                        dictionary.words.add(word);
                    } else {
                        System.out.println("Invalid line in the file: " + line);
                    }
                }
                scanner.close();
                System.out.println("Data loaded from file.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
    }

    public void dictionaryLookup() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the word to look up: ");
        String searchWord = scanner.nextLine().trim(); // Trim to remove leading/trailing whitespaces

        boolean found = false;

        for (Word word : dictionary.words) {
            if (word.getWordFound().equalsIgnoreCase(searchWord)) {
                System.out.println("Meaning: " + word.getWordExplaination());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Word not found in the dictionary.");
        }
    }

    public void addWordFromCommandline() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter English word to add: ");
        String wordFound = scanner.nextLine().trim();

        System.out.print("Enter Vietnamese meaning: ");
        String wordExplain = scanner.nextLine().trim();

        Word newWord = new Word(wordFound, wordExplain);
        dictionary.words.add(newWord);

        System.out.println("Word added to the dictionary.");
    }

    public void editWordFromCommandline() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the English word to edit: ");
        String searchWord = scanner.nextLine().trim();

        boolean found = false;

        for (Word word : dictionary.words) {
            if (word.getWordFound().equalsIgnoreCase(searchWord)) {
                System.out.print("Enter new Vietnamese meaning: ");
                String newWordExplain = scanner.nextLine().trim();

                // Update the meaning of the word
                word.setWordExplaination(newWordExplain);

                System.out.println("Word updated in the dictionary.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Word not found in the dictionary.");
        }
    }

    public void deleteWordFromCommandline() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the English word to delete: ");
        String searchWord = scanner.nextLine().trim();

        boolean found = false;

        for (Word word : dictionary.words) {
            if (word.wordFound.equalsIgnoreCase(searchWord)) {
                dictionary.words.remove(word);
                System.out.println("Word deleted from the dictionary.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Word not found in the dictionary.");
        }
    }

    public Trie getTrie() {
        return trie;
    }

    public void setTrie() {
        try {
            for (Word word : dictionary.words) {
                trie.insert(word.getWordFound());
            }
        } catch (NullPointerException e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    public void dictionaryExportToFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of file export: ");
        String fileName = scanner.nextLine().trim();
        boolean flag = false;
        try {
            File dict = new File("src/main/java"
                    + "/CMDDictionary/" + fileName + ".txt");
            if (dict.createNewFile()) {
                System.out.println("File created: " + dict.getName());
            } else {
                while (!flag) {
                    System.out.println("File already exists,do you still want to export to it: " + '\n'
                            + "1.YES" + '\n'
                            + "2.NO");
                    int ans = scanner.nextInt();
                    scanner.nextLine();
                    if (ans ==1) {
                        flag = true;
                    } else if (ans == 2) {
                        System.out.println("File already exists");
                        return;
                    } else {
                        System.out.println("Wrong input, type 1 or 2");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter("src/main/java"
                    + "/CMDDictionary/" + fileName + ".txt");
            Collections.sort(dictionary.words, (word1, word2) -> word1.wordFound.compareToIgnoreCase(word2.wordFound));
            for (Word word : dictionary.words) {
                writer.write(word.getWordFound() + "\t" + word.getWordExplaination() + "\n");
            }
            System.out.println("Dictionary exported to file: " + fileName);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error exporting dictionary to file: " + e.getMessage());
        }
        }

}




