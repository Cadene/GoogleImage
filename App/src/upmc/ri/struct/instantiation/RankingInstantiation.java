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


	
	public double[] psi2(List<double[]> x, RankingOutput y) {
		double [] p = VectorOperations.init(this.dim, 0);
		List<Integer> labels = y.getLabelsGT();
		List<Integer> labelsPos = new ArrayList<Integer>();
		List<Integer> labelsNeg = new ArrayList<Integer>();
		List<Integer> positionning = y.getPositionningFromRanking();
		for (int i = 0; i < x.size(); i++) {
			if (labels.get(i) >= 0) {
				labelsPos.add(i);
			} else {
				labelsNeg.add(i);
			}
		}
		for (int i = 0; i < labelsPos.size(); i++) {
			for (int j = 0; j < labelsNeg.size(); j++) {
				int yij;
				if (positionning.get(i) < positionning.get(j)) { // vérifier
					yij = 1;
				} else {
					yij = -1;
				}
				// p += yij * (xi - xj)
				for (int id = 0; id < x.get(i).length; id++) {
					p[id] += yij * (x.get(i)[id] - x.get(j)[id]);
				}
				//p = VectorOperations.add(p, VectorOperations.mult(yij, VectorOperations.minus(x.get(i), x.get(j))));
			}
		}
		return p;
	}
	
public double[] psi(List<double[]> x, RankingOutput y) {
		
		double[] psi = new double[x.get(0).length];
		
		int cptPos = 0; // compte le nombre de positif compté pour optimiser algorithme
		int cptNeg = 0; // compte le nombre de negatif compté pour optimiser algorithme
		List<Integer> labelsGT = y.getLabelsGT();
		List<Integer> ranking = y.getRanking();
		List<Integer> positionning = y.getPositionningFromRanking();
		
		for(int i=0; i<labelsGT.size(); i++){
			if(labelsGT.get(i)==1){
				cptPos++;
				cptNeg = 0;
				for(int j=0; j<labelsGT.size(); j++){
					if(labelsGT.get(j)==-1){
						cptNeg++;
						int signe = -1;
						if(positionning.get(i)<positionning.get(j)) signe = 1;
						for(int k=0; k<psi.length; k++){
							psi[k] += signe*(x.get(i)[k]-x.get(j)[k]);
						}
					}
					if(cptNeg==(labelsGT.size()-y.getNbPlus())) break; // Cas ou il n'y a plus de moins
				}
				if(cptPos==y.getNbPlus()) break; // Cas ou il n'y a plus de plus
			}
		}
		return psi;
	}

	@Override
	public double delta(RankingOutput y, RankingOutput yi) {
		return 1 - RankingFunctions.averagePrecision(yi);
	}


	@Override
	public Set<RankingOutput> enumerateY() {		
		return null;
	}

}
