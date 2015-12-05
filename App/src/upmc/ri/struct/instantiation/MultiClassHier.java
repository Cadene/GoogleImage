package upmc.ri.struct.instantiation;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;

public class MultiClassHier extends MultiClass {

	private double[][] distances;

	public MultiClassHier(Iterable<String> classes) {
		super(classes);
		int n = this.classes.size();
		this.distances = new double[n][n];
		ILexicalDatabase db = new NictWordNet();
		RelatednessCalculator calculator = new WuPalmer(db);
		double min = Double.MAX_VALUE;
		double max = - Double.MAX_VALUE;
		for (String word1 : classes) {
			int i = this.classes.get(word1);
			for (String word2 : classes) { 
				int j = this.classes.get(word2);
				// this.distances[i][j] = calculator.calcRelatednessOfWords(word1, word2);
				if (i == j) {
					this.distances[i][j] = 0;
				}
				else if (i < j){
					this.distances[i][j] = 1 - calculator.calcRelatednessOfWords(word1, word2);
					if (this.distances[i][j] < min){
						min = this.distances[i][j];
					}
					if (this.distances[i][j] > min){
						max = this.distances[i][j];
					}
				}
			}
		}
		// [a; b] -> [0.1 ; 2]
		// (1.9 * (x - a) / (b - a)) + .1
		for (int i = 1; i < n-1; i++){
			for (int j = i+1; j < n; j++){
				this.distances[i][j] = 1.9 * (this.distances[i][j] - min) / (max - min) + .1;
				this.distances[j][i] = this.distances[i][j];
			}
		}		
	}

	@Override
	public double delta(String y, String yi) {
		int idWord1 = this.classes.get(y);
		int idWord2 = this.classes.get(yi);
		return this.distances[idWord1][idWord2];
	}
}
