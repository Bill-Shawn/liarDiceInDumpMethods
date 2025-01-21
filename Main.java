import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.io.*;
import java.util.*;


public class Main {
    public static void main(String[] args)  throws IOException {
        // Create a file to store the output
        File outputFile = new File("output_rubric_1000000.txt");
        FileWriter fileWriter = new FileWriter(outputFile);
        PrintStream originalOut = System.out;

        // Create a PrintStream that writes to both console and file
        PrintStream fileAndConsole = new PrintStream(new MultiOutputStream(System.out, fileWriter));
        System.setOut(fileAndConsole); // Redirect System.out

        List<int[]> reports = new ArrayList<>();
        List<ConclusionReport> conclusionReports = new ArrayList<>();



        for (int sizing = 2; sizing < 9; sizing++)
        {
            System.out.println("\n--- Summary for  Sizing at "+ sizing + " ---");
            for (int bidingSelectionFactor = 1; bidingSelectionFactor < 10; bidingSelectionFactor++)
            {
                for(int questioningRate = 1; questioningRate < 10; questioningRate++)
                {
                    // times for one test 
                    for (int testingTimes = 0; testingTimes < 1000000; testingTimes++)
                    {
                        // player numbers, rate of selecting biding, rate of selecting questions
                        // the hight rate of i3/i4 will results into a quicker end of the game.
                        Game game = new Game(sizing, bidingSelectionFactor, 10,questioningRate,10);
                        int[] report = game.startGame(); // Collect report lines
                        reports.add(report);

                        
                        conclusionReports.add(new ConclusionReport(
                            report[0], // playerId
                            report[1], // winOrLoss
                            report[2], // bidCount
                            bidingSelectionFactor, // rateCondition1
                            questioningRate // rateCondition2
                        ));

                    }
                }
            }

            
            // Aggregate data by playerId, rateCondition1, and rateCondition2
            List<ConclusionReport> aggregatedReports = aggregateReports(conclusionReports);

            // Sort aggregated reports by win/loss in descending order, then bidCount
            Collections.sort(aggregatedReports, new Comparator<ConclusionReport>() {
                @Override
                public int compare(ConclusionReport o1, ConclusionReport o2) {
                    int winLossCompare = Integer.compare(o2.winOrLoss, o1.winOrLoss);
                    if (winLossCompare != 0) return winLossCompare;
                    return Integer.compare(o2.bidCount, o1.bidCount);
                }
            });


            // Print the top 30 aggregated results
            System.out.println("\n--- Top 30 Win/Loss Data ---");
            int totalWinningRounds = 0;
            for (int i = 0; i < Math.min(30, aggregatedReports.size()); i++) {
                ConclusionReport report = aggregatedReports.get(i);
                System.out.println(report);
                totalWinningRounds += report.winOrLoss;
            }

            System.out.println("\nTotal Winning Rounds in Top 30: " + totalWinningRounds);






            // Clear the data safely for the next sizing loop
            reports.clear();
            conclusionReports.clear();
            System.out.println("\nData cleared for the next sizing loop.");

        }

    }   



    private static List<ConclusionReport> aggregateReports(List<ConclusionReport> reports) {
        List<ConclusionReport> aggregated = new ArrayList<>();
        HashMap<AggregationKey, ConclusionReport> map = new HashMap<>();
    
        for (ConclusionReport report : reports) {
            // Create a unique key based on playerId, Rate 1, and Rate 2
            AggregationKey key = new AggregationKey(report.playerId, report.rateCondition1, report.rateCondition2);
            
            // Aggregate data using the key
            map.putIfAbsent(key, new ConclusionReport(report.playerId, 0, 0, report.rateCondition1, report.rateCondition2));
            ConclusionReport aggregatedReport = map.get(key);
            aggregatedReport.winOrLoss += report.winOrLoss;
            aggregatedReport.bidCount += report.bidCount;
        }
    
        aggregated.addAll(map.values());
        return aggregated;
    }
    
}




class MultiOutputStream extends OutputStream {
    private final OutputStream console;
    private final Writer fileWriter;

    public MultiOutputStream(OutputStream console, Writer fileWriter) {
        this.console = console;
        this.fileWriter = fileWriter;
    }

    @Override
    public void write(int b) throws IOException {
        console.write(b);
        fileWriter.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        console.write(b, off, len);
        fileWriter.write(new String(b, off, len));
    }

    @Override
    public void flush() throws IOException {
        console.flush();
        fileWriter.flush();
    }

    @Override
    public void close() throws IOException {
        console.close();
        fileWriter.close();
    }
}
