/* Brent Paterson
 * CSC131
 * Individual sprint 2, Metrics.java
 */

import picocli.CommandLine;

import java.util.*;

class Files {
    public long lines, words, chars, sources, comments;
    public String name;
    public boolean isSource;

    public Files() {
        lines = words = chars = sources = comments = 0;
        name = null;
        isSource = false;
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
    private static Boolean sourceFile = false;

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
            getOptions();
            try {
                run();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                instructions();
            }
        } else {
            // no args or help requested
            instructions();
        }
    }

    private static void run() throws Exception {
        //LinkedList<Files> initFiles = new LinkedList<>();
        MetricsApp metricsGetter;
        for (int i = 0; i < argsHolder.length; i++) {
            //Files f = new File();
            if (!argsHolder[i].startsWith("-")) {
                Files f = new Files();
                f.name = argsHolder[i];
                //initFiles.push(f);

                if (l) {
                    f.lines = MetricsApp.getLineCount(f.name);
                    totalLines += f.lines;
                }
                if (w) {
                    f.words = MetricsApp.getWordCount(f.name);
                    totalWords += f.words;
                }
                if (c) {
                    f.chars = MetricsApp.getCharacterCount(f.name);
                    totalChars += f.chars;
                }
                if (s) {
                    f.sources = MetricsApp.getSourceLineCount(f.name);
                    totalSources += f.sources;
                }
                if (C) {
                    f.comments = MetricsApp.getCommentLineCount(f.name);
                    totalComments += f.comments;
                }
                if (H);
                if (s || C) {
                    f.isSource = MetricsApp.isSource(f.name);
                    sourceFile = true;
                }

                filesHolder.push(f);
            }
        }

        output();

    }

    private static void getOptions() {
        for (int i = 0; i < argsHolder.length; i++) {
            if (argsHolder[i].startsWith("-")) {
                if (argsHolder[i].contains("l")) l = true;
                if (argsHolder[i].contains("w")) w = true;
                if (argsHolder[i].contains("c")) c = true;
                if (argsHolder[i].contains("s")) s = true;
                if (argsHolder[i].contains("C")) C = true;
                if (argsHolder[i].contains("H")) H = true;
            }
        }

        // no params, all true
        if (!l && !w && !c && !s && !C && !H)
            l = w = c = s = C = H = true;
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



    private static void output() {

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


            outputter(maxDigits, temp.lines, temp.words, temp.chars, temp.sources, temp.comments, temp.isSource);

            int prgmFileCounts = 0;
            if (s) prgmFileCounts++;
            if (C) prgmFileCounts++;

            // leave blank space for other prgmFile
            if (sourceFile && !temp.isSource)
                for (int i = 0; i < prgmFileCounts; i++)
                    for (int j = 0; j < maxDigits + 1; j++)
                        System.out.print(" ");

            System.out.print(temp.name + "\n");

        }

        // output total
        outputter(maxDigits, totalLines, totalWords, totalChars, totalSources, totalComments, sourceFile);
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