import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Triple;


public class CountStringSimilarity {
	//similarity measure strings
	private String jaccardS = "jaccard";
	private String jaroS = "jaro";
	private String scaledLevensteinS = "scaledLevenstein";
	private String tfidfS = "tfidf";
	private String jaroWinklerS ="jaroWinkler";
	private String exactMatchS = "exactMatch";


	public void run(ArrayList<String> classNames, ClassMapping cM, StringMeasures stringMeasures) {
		System.out.println("Start CountStringSimilarity.run()");
		long startTime = System.nanoTime();
		
		
		//for each class
		for (String className : classNames) {
			
			
			CountStringSimilarityResults results = new CountStringSimilarityResults();
		
			//instanceLabels: HashMap<k, <HashMap<kgClass,<HashMap<instanceURI, <HashSet<englishLabels>>>>
			HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels = new HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>();
			HashMap<String, ArrayList<String>> classMap = cM.getClassMap(className);//key: d,w,y,o,n ; value:kgC
			System.out.println(classMap);
			
			//get instances for each kgClass with all labels
			instanceLabels = getInstanceLabels(classMap);
			/*System.out.println(instanceLabels.get("d"));
			System.out.println(instanceLabels.get("y"));
			System.out.println(instanceLabels.get("n"));
			System.out.println(instanceLabels.get("o"));
			System.out.println(instanceLabels.get("w"));
			*/
			getMatchedStringCounts(results, instanceLabels, stringMeasures);
			
			//print results
			Set<Triple<String, String, String>> allTriples = results.getTriples();
			for (Triple<String,String,String> t : allTriples) {
				System.out.println(t.getLeft() + "_" + t.getMiddle() + "_" + t.getRight() + ":" + results.getInstanceOverlapCount(t.getLeft(), t.getMiddle(), t.getRight()));
			}
			
		}
		
		
		System.out.println("EXECUTION TIME: " +  ((System.nanoTime() - startTime)/1000000000) + " seconds." );
	}

	private void getMatchedStringCounts(
			CountStringSimilarityResults results, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels,
			StringMeasures stringMeasures) {
		//HashMap<String, HashMap<String, Integer>> results = new HashMap<String, HashMap<String, Integer>>();
		//for each kg
		for (String k : instanceLabels.keySet()) {
			switch (k) {
			case "d":
				compareDtoOthers(results, instanceLabels, stringMeasures);
				break;
			case "y":
				break;
			case "o":
				break;
			case "n":
				break;
			case "w":
				break;
			}
		}
				
		//return results;
	}

	private void compareDtoOthers(
			CountStringSimilarityResults results, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels,
			StringMeasures stringMeasures) {
		String fK = "d";
		
		//for each kgClass
		for (String kgClass :instanceLabels.get(fK).keySet()) {
			for (Entry<String, HashSet<String>> instanceWithLabels : instanceLabels.get(fK).get(kgClass).entrySet()) {
				compareLabelsWithYago(results, kgClass, instanceWithLabels.getValue(), instanceLabels.get("y"), stringMeasures);
				
				}
			}
	
		}
		
		
		//return null;
	private void compareLabelsWithYago(CountStringSimilarityResults results, String fromKgClass, HashSet<String> labels,
			HashMap<String, HashMap<String, HashSet<String>>> toKgClasses,
			StringMeasures stringMeasures) {	
		// for each label
		for (String label : labels) {
			if (label != null) {
			//for each kg class in yago
				for (String toKgClass : toKgClasses.keySet()) {
					//for each instance in yago
					for (Entry<String, HashSet<String>> yagoInstanceWithLabels : toKgClasses.get(toKgClass).entrySet()) {
						for (String yagoLabel : yagoInstanceWithLabels.getValue()) {
							if (yagoLabel != null) {
							
								//System.out.println(label + " AND " + yagoLabel);
								//System.out.println(stringMeasures.getSimilarityScores(label, yagoLabel));
								
								HashMap<String, Boolean> simResults = stringMeasures.getSimilarityResult(label, yagoLabel);
								//System.out.println(simResults);
								
								for (String simMeasureS : simResults.keySet()) {
									//check if match is true
									if(simResults.get(simMeasureS)) {
										//check which similarity measure
										if (simMeasureS.equals(jaccardS)) {
											results.addInstanceCount("d2y", fromKgClass, jaccardS);
										} else if (simMeasureS.equals(jaroS)) {
											results.addInstanceCount("d2y", fromKgClass, jaroS);
										} else if (simMeasureS.equals(scaledLevensteinS)) {
											results.addInstanceCount("d2y", fromKgClass, scaledLevensteinS);
										} else if (simMeasureS.equals(tfidfS)) {
											results.addInstanceCount("d2y", fromKgClass, tfidfS);
										} else if (simMeasureS.equals(jaroWinklerS)) {
											results.addInstanceCount("d2y", fromKgClass, jaroWinklerS);
										} else if (simMeasureS.equals(exactMatchS)) {
											results.addInstanceCount("d2y", fromKgClass, exactMatchS);
										}
									}
								}
									
							}
						}
					}
					
				}
			}
		}
		
	}


	

	private HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> getInstanceLabels(
			HashMap<String, ArrayList<String>> classMap) {
		HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels = new HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>();
		
		for (String k : classMap.keySet()) {
		    for (String kgClass : classMap.get(k)) {
		    	//System.out.println(kgClass);
		    	//get all instance labels for the kgClass and save them in the instanceLabels object
		    	HashMap<String, HashMap<String, HashSet<String>>> instanceLabelsForSingleKgClass = new HashMap<String, HashMap<String,HashSet<String>>>();
		    	instanceLabelsForSingleKgClass.put(kgClass, getInstanceLabelsForKgClass(k, kgClass)); 
		    	instanceLabels.put(k, instanceLabelsForSingleKgClass);
		    }
		}
		return instanceLabels;
	}

	private HashMap<String, HashSet<String>> getInstanceLabelsForKgClass(
			String k, String kgClass) {
		HashMap<String, HashSet<String>> instanceLabelsForSingleKgClass = new HashMap<String, HashSet<String>>();
		
		//get file paths 
		Path filePath = null;				
		
		//System.out.println(k + ": "+kgClass);
		switch (k) {
			case "d":
				
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/DBpedia/resultsWithLabelTest/");
				break;
			case "y":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/YAGO/resultsWithLabelTest/");
				break;
			case "o":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/OpenCyc/resultsWithLabelTest/");
				break;
			case "n":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/NELL/resultsWithLabelTest/");
				break;
			case "w":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/Wikidata/resultsWithLabelTest/");
				break;
			default:
				System.out.println("error in getInstanceLabelsForKgClass(). No matching k found");
		}
		instanceLabelsForSingleKgClass = readFile(filePath, kgClass);
		
		return instanceLabelsForSingleKgClass;
	}

	private HashMap<String, HashSet<String>> readFile(Path filePath,
			String kgClass) {
		HashMap<String, HashSet<String>> instanceLabelsForSingleKgClass = new HashMap<String, HashSet<String>>();
		Path fileName = Paths.get(filePath + "/" + kgClass + "InstancesWithLabels.txt");
		try (Stream<String> stream = Files.lines(fileName)) {
			stream.forEach(line -> addLineToHashMap(line, kgClass, instanceLabelsForSingleKgClass));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instanceLabelsForSingleKgClass;
	}

	private static void addLineToHashMap(String line, String kgClass,
			HashMap<String, HashSet<String>> instanceLabelsForSingleKgClass) {
		String[] words = line.split("\\t");
		HashSet<String> allLabels = new HashSet<String>();
		for (int i = 1; i < words.length; i++) {
			allLabels.add(words[i]);
			//System.out.println(words[i]);
		}
		
		instanceLabelsForSingleKgClass.put(words[0], allLabels);		
	}

}
