# **English dictionary for learning purposes** 
## 1. Introduction 
This is a major assignment group project for our Object-oriented programming (OOP) class coding in 100% Java language. It contained all the basic functions expected of a common dictionary as well as some interesting game that was made by our group. There are 2 versions, the command line version and the graphic version
**Class Diragram is represented via this link:** https://github.com/ngoctuannguyen/JDictionary-E-V1/blob/main/src/main/resources/InheritanceTree.svg
## 2. Set up 
**As of this moment, the app can only be run on IDE, we advise using Inteliji for this** 
### 2.1. The command line version 
Simply open the CMDDictionary package and run the DictionaryCommandline class. To save your progress, after you are done use the export function to save it into a .txt file (you may use the dictionaries.txt that we already made or just export into a new .txt file). When you open it again, use the import function to load back the file into the dictionary
![image](https://hackmd.io/_uploads/Sy_GgLw86.png) 
### 2.2 The graphic version 
Open jdictionaryev1 package and run HelloApplication class 
## 3.Project Overview 
The graphic version is an evolved version of the commandline version so we will only talk about it. We took references and ideas from our teacher's guidelines as well as some existing dictionaries such as Cambridge, Oxford,...The app has the following main functions: 
*  Beautiful and user-friendly UI with gradient color made by Javafx and designed on Scene Builder 
![image](https://hackmd.io/_uploads/rycZZ8vLT.png) 
*  Instead of .txt file like the command line version, we use the database to save Word so we don't have to export and import every time we use it. Credit to https://github.com/yenthanh132/avdict-database-sqlite-converter for providing us with a rich English-Vietnamese vocabulary database 
*  The search function utilized search queries of SQL and we also optimized it further by using Trie to display all words in the database having same pattern as the word in the search bar 
* Basic function Add Word, Delete Word and Edit Word. They will also give you warnings for specific cases (adding existing words, deleting/editing non-existing words) 
* Spelling function for every word 
* Translate scenes like Google Translate help you translate a single word or sentence from English to Vietnamese and reverse (We also support Chinese, Japanese, Koreans) 
![image](https://hackmd.io/_uploads/rygrWIwLp.png) 
* Game for studying purpose (will be discussed more below) 
## 4. About Game 
The time and effort we make for our game have exceeded even our expectations and we feel it is appropriate to explain the game in a separate section. There are two games: Quiz Game and Olympia Game 
### 1.1. Quiz Game 
For each playthrough your mission is to answer 10 choice questions taken randomly from the database, there will also be feedback depending on your result. We also support adding custom questions to your liking
![Screenshot 2023-12-13 224423](https://hackmd.io/_uploads/r1btWLPLa.png) 
### 1.2.Olympia Game 
Taken inspiration from a famous game in our country Viet Nam https://vi.wikipedia.org/wiki/%C4%90%C6%B0%E1%BB%9Dng_l%C3%AAn_%C4%91%E1%BB%89nh_Olympia (You may paste it into our dictionary translate screen if you do not understand Vietnamese). 
![image](https://hackmd.io/_uploads/Hkf3-8DLp.png) 
Based on that, our game have 4 main round, the rule of each round will be explained before beginning or you can see it in the tutorial at the game menu:
* Round 1: Answer questions by typing your answer 
* Round 2: Answer questions to gradually open the hidden picture and give the keyword base on that picture. 
* Round 3: For each question you are given a set of images that will be displayed one after another, after that you will have to answer the keyword related to those 
* Round 4: Choose 3 questions from 3 packages 10,20,30 (you can pick however you like but the higher the score the harder the question will be and wrong answer will result in half of the point of that question will be deducted from your score ). There is also a star hope which is a one-time used button that will give you double the point should you answer the next question correctly. 
## 5.Ideas for improvement 
* Smart vocab: Can display words that are synonyms or antonyms (maybe both) 
* Search by topic: For example when searching country will display America, England, VietNam,.. 
* Support Grammar also 
* Adjust Olympia Game so that it can be played in multi-player (we try but failed) 
* Destroying hidden bug (We make sure to clean up every bug we could find but you never know) 
* ... 
Albert Einstein once said: 
> The true sign of intelligence is not knowledge but imagination 

so we also hope that you can provide us with new ideas and improvement 
### 6.Credit 
**This project was made by a team of 3 members:** 
1. Nguyễn Tuấn Ngọc - 22026521 
2. Nguyễn Văn Quân - 22026552 
3. Đỗ Hữu Hoàng Tùng - 22026551 
 
**And also special thanks to our teachers in OOP class for providing us with guidelines and necessary knowledge in Java and OOP to do this**
