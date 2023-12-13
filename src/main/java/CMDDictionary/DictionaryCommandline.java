package CMDDictionary;


import java.util.List;
import java.util.Scanner;


public class DictionaryCommandline {
    public DictionaryManagement dictionaryManagement;

    public DictionaryCommandline() {
        dictionaryManagement = new DictionaryManagement();
    }
    public void DictionaryBasic() {
        dictionaryManagement.insertFromCommandline();
        dictionaryManagement.showAllWords();
    }


    public void dictionarySearcher() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the word to look up: ");
        String searchWord = scanner.nextLine().trim();
        dictionaryManagement.setTrie();
        List<String> list = dictionaryManagement.getTrie().autoComplete(searchWord);
        if (list == null) {
            System.out.println("Word not found in the dictionary.");
        } else {
            for (String s : list) {
                System.out.println(s);
            }
        }
    }

    public void dictionaryAdvance()  {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("Welcome to My Application!\n" +
                    "[0] Exit\n" +
                    "[1] Add\n" +
                    "[2] Remove\n" +
                    "[3] Update\n" +
                    "[4] Display\n" +
                    "[5] Lookup\n" +
                    "[6] Search\n" +
                    "[7] Game\n" +
                    "[8] Import from file\n" +
                    "[9] Export to file\n" +
                    "Your action:");
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    flag = false;
                    System.out.println("Exist successfully");
                    break;
                case 1:
                    dictionaryManagement.addWordFromCommandline();
                    break;
                case 2:
                    dictionaryManagement.deleteWordFromCommandline();
                    break;
                case 3:
                    dictionaryManagement.editWordFromCommandline();
                    break;
                case 4:
                    dictionaryManagement.showAllWords();
                    break;
                case 5:
                    dictionaryManagement.dictionaryLookup();
                    break;
                case 6:
                    this.dictionarySearcher();
                    break;
                case 7:
                    System.out.println("Please visit our graphic version for some exciting games: ");
                    break;
                case 8:
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.println("Enter file's name that you want to import: ");
                    String fileName = scanner2.nextLine();
                    dictionaryManagement.insertFromFile(fileName);
                    break;
                case 9:
                    dictionaryManagement.dictionaryExportToFile();
                    break;
                default:
                    System.out.println("Action not supported.");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Interrupt Error");
            }
        }

    }
  public static void main(String[] args) {
      DictionaryCommandline dictionaryCommandline = new DictionaryCommandline();
      dictionaryCommandline.dictionaryAdvance();
  }
 }


