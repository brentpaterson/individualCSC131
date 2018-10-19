/* Brent Paterson
 * CSC131
 * Individual sprint 2, Metrics.java
 */
import picocli.CommandLine;

import java.io.*;
import java.util.*;

class Files {
    public long lines, words, chars, sources, comments;
    public String name;

    public Files() {
        lines = words = chars = sources = comments = 0;
        name = null;
    }

    public Files(String n, long l, long w, long c, long s, long com) {
        name = n;
        lines = l;
        words = w;
        chars = c;
        sources = s;
        comments = com;
    }
}

class Halstead {
    protected long littleN1, littleN2, bigN1, bigN2;
    protected long nVocabulary, nLength;
    protected double nCalcLength, nVolume, nDifficulty, nEffort, nTime, nBugs;

    public Halstead() {
        littleN1 = littleN2 = bigN1 = bigN2 = nVocabulary = nLength = 0;
        nCalcLength = nVolume = nDifficulty = nEffort = nTime = nBugs = 0;
    }

    public Halstead (long littleN1, long littleN2, long bigN1, long bigN2) {
        this.littleN1 = littleN1;
        this.littleN2 = littleN2;
        this.bigN1 = bigN1;
        this.bigN2 = bigN2;

        // only fncs need to be ran as they call each other
        /* confirm everything will default to 0 if not specified? */
        nCalcLength = getNCalcLength();
        nTime = getNTime();
        nBugs = getNBugs();
    }

    public long getNVocab () {
        return littleN1 + littleN2;
    }

    public long getNLength () {
        return bigN1 + bigN2;
    }

    public double getNCalcLength () {
        return (littleN1 * (Math.log(littleN1) / Math.log(2)) + littleN2 * (Math.log(littleN2) / Math.log(2)));
    }

    public double getNVolume () {
        if (nLength == 0)
            nLength = getNLength();
        if (nVocabulary == 0)
            nVocabulary = getNVocab();

        return nLength * (Math.log(nVocabulary) / Math.log(2));
    }

    public double getNDifficulty() {
        return ((double) littleN1 / 2) + ((double)bigN2 / (double) littleN2);
    }

    public double getNEffort() {
        if (nDifficulty == 0)
            nDifficulty = getNDifficulty();
        if (nVolume == 0)
            nVolume = getNVolume();

        return nDifficulty * nVolume;
    }

    public double getNTime() {
        if (nEffort == 0)
            nEffort = getNEffort();

        return nEffort / 18;
    }

    public double getNBugs() {
        if (nVolume == 0)
            nVolume = getNVolume();

        return nVolume / 3000;
    }
}

public class Metrics {
    private static String[] argsHolder;
    private static long totalLines = 0;
    private static long totalWords = 0;
    private static long totalChars = 0;
    private static long totalSources = 0;
    private static long totalComments = 0;
    @CommandLine.Option(names = "-l", usageHelp = true, description = "Display number of lines")
    private static Boolean l = false;
    @CommandLine.Option(names = "-w", usageHelp = true, description = "Display number of words")
    private static Boolean w = false;
    @CommandLine.Option(names = "-c", usageHelp = true, description = "Display number of characters")
    private static Boolean c = false;
    @CommandLine.Option(names = "-s", usageHelp = true, description = "Display number of source lines")
    private static Boolean s = false;
    @CommandLine.Option(names = "-C", usageHelp = true, description = "Display number of comment lines")
    private static Boolean C = false;
    @CommandLine.Option(names = "-H", usageHelp = true, description = "Display Halstead's metrics")
    private static Boolean H = false;
    private static Boolean programmingFile = false;

    @CommandLine.Parameters(paramLabel = "FILES", description = "Files to parse")
    private static LinkedList<Files> filesHolder = new LinkedList<Files>();

    public static void main(String[] args) {
        // check for help request
        Boolean h = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-' && args[i].contains("h")) {
                h = true;
                break;
            }
        }

        argsHolder = args;
        if (args.length > 0 && !h) {
            analyzeFile();
        } else {
            // no args or help requested
            instructions();
        }
    }

    private static void instructions() {
        System.out.println("wc will print instructions for how to use wc \n" +
                "wc -l <filename> will print the line count of a file\n" +
                "wc -c <filename> will print the character count\n" +
                "wc -w <filename> will print the word count\n" +
                "wc -s <filename> will print the source lines count\n" +
                "wc -C <filename> will print the comment lines count\n" +
                "wc -H <filename> will print Halstead's metrics\n" +
                "wc -h <filename> will print this message\n" +
                "wc <filename> will print all of the above");
    }

    private static void analyzeFile() {
        Boolean fileExists = false;
        for (int i = 0; i < argsHolder.length; i++) {
            // is a file?
            if (argsHolder[i].charAt(0) != '-') {
                fileExists = true;
                runFile(argsHolder[i]);
                if (programFileChecker(argsHolder[i]))
                    programmingFile = true;
            }
        }

        // output total if there was a file, otherwise instructions
        if (fileExists) {
            output();
        } else {
            instructions();
        }
    }

    private static void runFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            long lines, words, chars, sources, comments;
            lines = words = chars = sources = comments = 0;
            boolean commentLine = false;

            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    lines++;
                    chars += line.length();

                    // get words in line
                    String trim = line.trim();

                    if (!trim.isEmpty()) {
                        // check if and how many source line
                        if (!commentLine && !trim.startsWith("//") && !trim.startsWith("/*")) {
                            sources++;
                        }

                        // check if comment line
                        if (commentLine)
                            comments++;
                        if (trim.contains("//") && !commentLine)
                            comments++;
                        else if (trim.contains("/*") && !commentLine) {
                            comments++;
                            commentLine = true;
                            if (trim.contains("*/"))
                                commentLine = false;
                        } else if (trim.contains("*/"))
                            commentLine = false;



                        words += trim.split("\\s+").length;
                    }
                } else {
                    break;
                }
            }

            totalLines += lines;
            totalWords += words;
            totalChars += chars;
            if (programFileChecker(file)) {
                totalSources += sources;
                totalComments += comments;
            }

            Files temp = new Files(file, lines, words, chars, sources, comments);
            filesHolder.add(temp);
        } catch (Exception e) {
            System.out.println("\nError: " + e + "\n");
            instructions();
            System.exit(-1);
        }
    }

    private static void output() {

        // find what params were given for output
        for (int i = 0; i < argsHolder.length; i++) {
            if (argsHolder[i].charAt(0) == '-') {
                if (argsHolder[i].contains("l"))
                    l = true;
                if (argsHolder[i].contains("w"))
                    w = true;
                if (argsHolder[i].contains("c"))
                    c = true;
                if (argsHolder[i].contains("s"))
                    s = true;
                if (argsHolder[i].contains("C"))
                    C = true;
                if (argsHolder[i].contains("H"));
                    H = true;
            }
        }

        // no params, all true
        if (!l && !w && !c && !s && !C && !H) {
            l = w = c = s = C = H = true;
        }

        long max = 0;
        if (l)
            max = Math.max(totalLines, max);
        if (w)
            max = Math.max(totalWords, max);
        if (c)
            max = Math.max(totalChars, max);
        if (s)
            max = Math.max(totalSources, max);
        if (C)
            max = Math.max(totalComments, max);
        if (H)
            ; // to be added

        int maxDigits = String.valueOf(max).length();

        headerOutput(maxDigits);

        // output each file
        while (!filesHolder.isEmpty()) {
            Files temp = filesHolder.pop();

            boolean prgmFile = programFileChecker(temp.name);

            outputter(maxDigits, temp.lines, temp.words, temp.chars, temp.sources, temp.comments, prgmFile);

            int prgmFileCounts = 0;
            if (s) prgmFileCounts++;
            if (C) prgmFileCounts++;

            // leave blank space for other prgmFile
            if (programmingFile && !prgmFile)
                for (int i = 0; i < prgmFileCounts; i++)
                    for (int j = 0; j < maxDigits + 1; j++)
                        System.out.print(" ");

            System.out.print(temp.name + "\n");

        }

        // output total
        outputter(maxDigits, totalLines, totalWords, totalChars, totalSources, totalComments, programmingFile);
        System.out.print("total\n");

    }

    private static void outputter(int maxDigits, long lines, long words, long chars,
                                  long sources, long comments, boolean prgmFile) {
        if (l) {
            spacePrinter(maxDigits, lines);
            System.out.print(lines + " ");
        }
        if (w) {
            spacePrinter(maxDigits, words);
            System.out.print(words + " ");
        }
        if (c) {
            spacePrinter(maxDigits, chars);
            System.out.print(chars + " ");
        }
        if (s && prgmFile) {
            spacePrinter(maxDigits, sources);
            System.out.print(sources + " ");
        }
        if (C && prgmFile) {
            spacePrinter(maxDigits, comments);
            System.out.print(comments + " ");
        }
    }

    private static boolean programFileChecker(String name) {
        if (name.endsWith(".java")
                || name.endsWith(".cpp")
                || name.endsWith(".c")
                || name.endsWith(".class")
                || name.endsWith(".hpp")
                || name.endsWith(".h")) {
            return true;
        }
        return false;
    }

    private static void spacePrinter(int maxDigits, long num) {
        for (int i = Math.min(String.valueOf(num).length(), maxDigits); i < maxDigits; i++)
            System.out.print(" ");
    }

    private static void headerOutput(int maxDigits) {

        boolean ranOnce = false;
        if (l) {
            for (int i = 0; i < maxDigits - 1; i++)
                System.out.print(" ");
            System.out.print("l");
            ranOnce = true;
        }

        if (w) {
            for (int i = 0; i < maxDigits - 1; i++)
                System.out.print(" ");
            if (ranOnce)
                System.out.print(" ");
            System.out.print("w");
            ranOnce = true;
        }

        if (c) {
            for (int i = 0; i < maxDigits - 1; i++)
                System.out.print(" ");
            if (ranOnce)
                System.out.print(" ");
            System.out.print("c");
            ranOnce = true;
        }

        if (s) {
            for (int i = 0; i < maxDigits - 1; i++)
                System.out.print(" ");
            if (ranOnce)
                System.out.print(" ");
            System.out.print("s");
            ranOnce = true;
        }

        if (C) {
            for (int i = 0; i < maxDigits - 1; i++)
                System.out.print(" ");
            if (ranOnce)
                System.out.print(" ");
            System.out.print("C");
        }
        System.out.print("\n");
    }
}