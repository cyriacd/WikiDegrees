package com.cyriacdomini.wikidegrees;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        long startTime = System.nanoTime();
        String startTopic = WikiDegrees.getMostRelevantTopic(args[0]);
        String endTopic = WikiDegrees.getMostRelevantTopic(args[1]);
        if(startTopic==null || endTopic ==null){
          System.err.println("Unable to find topics!");
          System.exit(1);
        }
        System.out.println("Start Topic: "+startTopic+" End Topic: "+endTopic);
        new WikiDegreesBFS().findDegreesOfSeparation(startTopic,endTopic);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Time: "+ duration/1000);
    }
}
