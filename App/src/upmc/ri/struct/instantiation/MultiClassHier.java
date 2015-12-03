package upmc.ri.struct.instantiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.MatrixVisualization;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.WS4J;
import edu.cmu.lti.ws4j.impl.WuPalmer;

public class MultiClassHier extends MultiClass {

	private double[][] distances;

	public MultiClassHier(Iterable<String> classes) {
		super(classes);
		int n = this.classes.size();
		this.distances = new double[n][n];
		ILexicalDatabase db = new NictWordNet();
		RelatednessCalculator calculator = new WuPalmer(db);
		for (String word1 : classes) {
			int i = this.classes.get(word1);
			for (String word2 : classes) {
				int j = this.classes.get(word2);
				this.distances[i][j] = calculator.calcRelatednessOfWords(word1, word2);
			}
		}
	}

	@Override
	public double delta(String y, String yi) {
		int idWord1 = this.classes.get(y);
		int idWord2 = this.classes.get(yi);
		if (idWord1 == idWord2){
			return 1 - this.distances[idWord1][idWord2];
		}
		return 0;
	}

	@Override
	public Set<String> enumerateY() {
		return classes.keySet();
	}

}