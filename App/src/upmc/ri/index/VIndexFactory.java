package upmc.ri.index;

import java.util.List;

import upmc.ri.utils.VectorOperations;

public class VIndexFactory {

	public static double[] computeBow(List<Integer> words, int dictSize) {
		/* Initialiser le BoW */ 
		double[] bow = new double[dictSize];
		for (int i = 0; i < dictSize; i++) {
			bow[i] = 0.0;
		}
		
		/* Construire l'histogramme */
		for (int i = 0; i < words.size(); i++) {
			bow[words.get(i)] += 1;
		}
		/*
		int id_cluster = words.get(0);
		for (int i = 0; i < words.size(); i++) {
			if (id_cluster < words.get(i)) {
				id_cluster = words.get(i);
			}
			bow[id_cluster] += 1;
		}*/
	
		/* Normalisation l2 */
		double norm = VectorOperations.norm2(bow);
		for (int i = 0; i < dictSize; i++) {
			bow[i] /= norm;
		}
		
		return bow;
	}
	
	public static double[] computeBow(List<Integer> words){
		int dictSize = 1000;
		//Collections.sort(words);
		//int dictSize = words.get(words.size() - 1) + 1;
		
		return VIndexFactory.computeBow(words,dictSize);
	}
}
