package upmc.ri.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import upmc.ri.utils.VectorOperations;

public class VIndexFactory {

	public static double[] computeBow(List<Integer> words) {
		Collections.sort(words);
		int nmax = words.get(words.size() - 1) + 1;
		double[] bow = new double[nmax];
		for (int i = 0; i < nmax; i++) {
			bow[i] = 0.0;
		}
		int id_cluster = words.get(0);
		for (int i = 0; i < words.size(); i++) {
			if (id_cluster < words.get(i)) {
				id_cluster = words.get(i);
			}
			bow[id_cluster] += 1;
		}
		double norm = VectorOperations.norm2(bow);
		for (int i = 0; i < nmax; i++) {
			bow[i] /= norm;
		}
		return bow;
	}
	
}
