package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MetricsApp implements IMetrics {

    boolean setPath(String path);   // sets the file path to process
    // returns true if current path is valid
    boolean isSource();             // returns true if the file is a source file

    // basic counts for any file
    //
    int getLineCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int lines = 0;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            else
                lines++;
        }

        return lines;
    }

    int getWordCount(String file) {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        int words = 0;

        while (true) {
            String line = reader.readLine();
            String trim = line.trim();

            words += trim.split("\\s+").length;
        }

        return words;
    }
    int getCharacterCount();

    // source code line counts
    //
    int getSourceLineCount();
    int getCommentLineCount();

    // Halstead metrics
    //
    int getHalsteadn1();            // number of distinct operands
    int getHalsteadn2();            // number of distinct operators
    int getHalsteadN1();            // number of operands
    int getHalsteadN2();            // number of operators

    int getHalsteadVocabulary();
    int getHalsteadProgramLength();
    int getHalsteadCalculatedProgramLenght();
    int getHalsteadVolume();
    int getHalsteadDifficulty();
    int getHalsteadEffort();
    int getHalsteadTime();
    int getHalsteadBugs();
}
