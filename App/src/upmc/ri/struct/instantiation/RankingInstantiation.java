package upmc.ri.struct.instantiation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.MatrixVisualization;

import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.utils.VectorOperations;

public class RankingInstantiation implements IStructInstantiation<List<double[]>, RankingOutput> {
	
	
	private int dim;

	public RankingInstantiation(int dim) {
		super();
		this.dim = dim;
	}


	@Override
	public double[] psi(List<double[]> x, RankingOutput y) {
		double [] p = VectorOperations.init(this.dim, 0);
		List<Integer> labels = y.getLabelsGT();
		List<Integer> labelsPos = new ArrayList<Integer>();
		List<Integer> labelsNeg = new ArrayList<Integer>();
		List<Integer> positionning = y.getPositionningFromRanking();
		for (int i = 0; i < x.size(); i++) {
			if (labels.get(i) > 0) {
				labelsPos.add(i);
			} else {
				labelsNeg.add(i);
			}
		}
		for (int i = 0; i < labelsPos.size(); i++) {
			for (int j = 0; j < labelsNeg.size(); j++) {
				int yij;
				if (positionning.get(i) < positionning.get(j)) {
					yij = 1;
				} else {
					yij = -1;
				}
				// p += yij * (xi - xj)
				p = VectorOperations.add(p, VectorOperations.mult(yij, VectorOperations.minus(x.get(i), x.get(j))));
			}
		}
		return p;
	}

	@Override
	public double delta(RankingOutput y, RankingOutput yi) {
		return 1 - RankingFunctions.averagePrecision(y);
	}


	@Override
	public Set<RankingOutput> enumerateY() {		
		return null;
	}

}
