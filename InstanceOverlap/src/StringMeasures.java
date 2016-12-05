import com.wcohen.ss.*;

import java.util.*;
import java.util.Map.Entry;

public class StringMeasures {
	//boolean
	private boolean jaccard;
	private boolean jaro;
	private boolean scaledLevenstein;
	private boolean tfidf;
	private boolean jaroWinkler;
	private boolean exactMatch;
	private boolean softTfidf;
	//classes
	private Jaccard jaccardC;
	private Jaro jaroC;
	private ScaledLevenstein scaledLevensteinC;
	private TFIDF tfidfC;
	private JaroWinkler jaroWinklerC;
	private SoftTFIDF softTfidfC;
	//thresholds
	private double jaccardT;
	private double jaroT;
	private double scaledLevensteinT;
	private double tfidfT;
	private double jaroWinklerT;
	private double softTfidfT;
	//strings
	private String jaccardS = "jaccard";
	private String jaroS = "jaro";
	private String scaledLevensteinS = "scaledLevenstein";
	private String tfidfS = "tfidf";
	private String jaroWinklerS ="jaroWinkler";
	private String exactMatchS = "exactMatch";
	private String softTfidfS = "softTfidf";
	
	/**
	   * StringMeasures constructor
	   * @param exactMatch boolean
	   * @param jaccard boolean
	   * @param jaccardT threshold value (double)
	   * @param jaro boolean
	   * @param jaroT threshold value (double)
	   * @param scaledLevenstein boolean
	   * @param scaledLevensteinT threshold value (double)
	   * @param tfidf boolean
	   * @param tfidfT threshold value (double)
	   * @param jaroWinkler boolean
	   * @param jaroWinklerT threshold value (double)
	   * @param softTfidf boolean
	   * @param softTfidfT threshold value (double) 
	   */
	public StringMeasures(boolean exactMatch, boolean jaccard, double jaccardT, boolean jaro, double jaroT, boolean scaledLevenstein, double scaledLevensteinT, boolean tfidf, double tfidfT, boolean jaroWinkler, double jaroWinklerT, boolean softTfidf, double softTfidfT) {		
		this.exactMatch = exactMatch;
		this.jaccard = jaccard;
		this.jaro = jaro;
		this.scaledLevenstein = scaledLevenstein;
		this.tfidf = tfidf;
		this.jaroWinkler = jaroWinkler;
		this.softTfidf = softTfidf;
		
		if (jaccard) {
			this.jaccardC = new Jaccard();
			this.jaccardT = jaccardT;		
		}
		if (jaro) {
			this.jaroC = new Jaro();
			this.jaroT = jaroT;
		}
		if (scaledLevenstein) {
		 this.scaledLevensteinC = new ScaledLevenstein();
		 this.scaledLevensteinT = scaledLevensteinT;
		}
		if (tfidf) {
			this.tfidfC = new TFIDF();
			this.tfidfT = tfidfT;		
		}
		if (jaroWinkler) {
			this.jaroWinklerC = new JaroWinkler();
			this.jaroWinklerT = jaroWinklerT;
		}
		if (softTfidf) {
			this.softTfidfC = new SoftTFIDF();
			this.softTfidfT = softTfidfT;
		}
	}
	
	public double getJaccardScore(String s1, String s2) {
		return jaccardC.score(jaccardC.prepare(s1), jaccardC.prepare(s2));
	}
	public double getJaroScore(String s1, String s2) {
		return jaroC.score(jaroC.prepare(s1), jaroC.prepare(s2));
	}
	public double getScaledLevenstein(String s1, String s2) {
		return scaledLevensteinC.score(scaledLevensteinC.prepare(s1), scaledLevensteinC.prepare(s2));
	}
	public double getTfidfScore(String s1, String s2) {
		return tfidfC.score(tfidfC.prepare(s1), tfidfC.prepare(s2));
	}
	public double getJaroWinklerScore(String s1, String s2) {
		return jaroWinklerC.score(jaroWinklerC.prepare(s1), jaroWinklerC.prepare(s2));
	}
	public double getSoftTfidfScore(String s1, String s2) {
		return softTfidfC.score(softTfidfC.prepare(s1), softTfidfC.prepare(s2));
	}
	
	/**
	   * Get string equality score
	   * @param s1
	   * @param s2
	   * @return double (1.0 if strings are equal; 0.0 otherwise)
	   */
	public double getExactMatchScore(String s1, String s2) {
		double score = 0.0;
		if (s1.equals(s2)) {
			score = 1.0;
		}
		return score;
	}
	/**
	   * Check if two strings are equal
	   * @param s1
	   * @param s2
	   * @return boolean
	   */
	public boolean getExactMatch(String s1, String s2) {
		if (s1.equals(s2)) { 
			return true;
		}
		return false;
	}
	/**
	   * Get similarity scores for two strings s1 and s2
	   * @param s1
	   * @param s2
	   * @return HashMap<String, Double> with similarity measure name and result score
	   */
	public HashMap<String, Double> getSimilarityScores(String s1, String s2) {
		HashMap<String, Double> resultScores = new HashMap<String, Double>();
		if (this.exactMatch) {
			resultScores.put(this.exactMatchS, getExactMatchScore(s1, s2));
		}
		if (this.jaccard) {
			resultScores.put(this.jaccardS, getJaccardScore(s1, s2));
		}
		if (this.jaro) {
			resultScores.put(this.jaroS, getJaroScore(s1, s2));
		}
		if (this.scaledLevenstein) {
			resultScores.put(this.scaledLevensteinS, getScaledLevenstein(s1, s2));
		}
		if (this.tfidf) {
			resultScores.put(this.tfidfS, getTfidfScore(s1, s2));
		}
		if (this.jaroWinkler) {
			resultScores.put(this.jaroWinklerS, getJaroWinklerScore(s1, s2));
		}	
		if (this.softTfidf) {
			resultScores.put(this.softTfidfS, getSoftTfidfScore(s1, s2));
		}
		return resultScores;
	}
	/**
	   * Get similarity results for two strings s1 and s2
	   * @param s1
	   * @param s2
	   * @return HashMap<String, Boolean> with similarity measure name and result
	   */
	public HashMap<String, Boolean> getSimilarityResult(String s1, String s2) {
		HashMap<String, Boolean> results = new HashMap<String, Boolean>();
		if (this.exactMatch) {
			results.put(this.exactMatchS, getExactMatch(s1, s2));
		}
		//check scores against thresholds
		HashMap<String, Double> resultScores = getSimilarityScores(s1, s2);
		//get all scores
		for (Entry<String, Double> entry : resultScores.entrySet()) {
			//check sim measure & check threshold
			if (entry.getKey().equals(this.jaccardS)) {
				results.put(this.jaccardS, checkThreshold(entry.getValue().doubleValue(), this.jaccardT));
				if (checkThreshold(entry.getValue().doubleValue(), this.jaccardT))
					System.out.println(s1 + " matched with " + s2);
			} else if (entry.getKey().equals(this.jaroS)) {
				results.put(this.jaroS, checkThreshold(entry.getValue().doubleValue(), this.jaroT));
				if (checkThreshold(entry.getValue().doubleValue(), this.jaroT))
					System.out.println(s1 + " matched with " + s2);
			} else if (entry.getKey().equals(this.scaledLevensteinS)) {
				results.put(this.scaledLevensteinS, checkThreshold(entry.getValue().doubleValue(), this.scaledLevensteinT));
				if (checkThreshold(entry.getValue().doubleValue(), this.scaledLevensteinT))
					System.out.println(s1 + " matched with " + s2);
			} else if (entry.getKey().equals(this.tfidfS)) {
				results.put(this.tfidfS, checkThreshold(entry.getValue().doubleValue(), this.tfidfT));	
				if (checkThreshold(entry.getValue().doubleValue(), this.tfidfT))
					System.out.println(s1 + " matched with " + s2);
			} else if (entry.getKey().equals(this.jaroWinklerS)) {
				results.put(this.jaroWinklerS, checkThreshold(entry.getValue().doubleValue(), this.jaroWinklerT));
				if (checkThreshold(entry.getValue().doubleValue(), this.jaroWinklerT))
					System.out.println(s1 + " matched with " + s2);
			} else if (entry.getKey().equals(this.softTfidfS)) {
				results.put(this.softTfidfS, checkThreshold(entry.getValue().doubleValue(), this.softTfidfT));
				if (checkThreshold(entry.getValue().doubleValue(), this.softTfidfT))
					System.out.println(s1 + " matched with " + s2);
			}
		}
		return results;
	}
	/**
	   * Check if value is at least the threshold (v>=t) 
	   * @return boolean
	   */
	private boolean checkThreshold(double v, double t) {
		boolean r = false;
		if (v>=t)
			r = true;	
		return r;
	}
	/**
	   * Get blank HashMap with all similarity measures 
	   * @return HashMap
	   */
	public HashMap<String, Boolean> getBlankInstanceResultsContainer() {
		HashMap<String, Boolean> instanceResults = new HashMap<String, Boolean>();
		if (this.exactMatch) {
			instanceResults.put(this.exactMatchS, false);
		}	
		if (this.jaccard) {
			instanceResults.put(this.jaccardS, false);
		}
		if (this.jaro) {
			instanceResults.put(this.jaroS, false);
		}
		if (this.scaledLevenstein) {
			instanceResults.put(this.scaledLevensteinS, false);
		}
		if (this.tfidf) {
			instanceResults.put(this.tfidfS, false);
		}
		if (this.jaroWinkler) {
			instanceResults.put(this.jaroWinklerS, false);
		}
		if (this.softTfidf) {
			instanceResults.put(this.softTfidfS, false);
		}
		return instanceResults;
	}

}