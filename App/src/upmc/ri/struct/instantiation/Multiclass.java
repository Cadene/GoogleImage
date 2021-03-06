package upmc.ri.struct.instantiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.MatrixVisualization;

public class Multiclass implements IStructInstantiation<double[], String> {

	protected Map<String, Integer> classes;
	
	public Multiclass(Iterable<String> classes) {
		super();
		int i = 0;
		this.classes = new HashMap<String, Integer>();
		for (String classe : classes){
			this.classes.put(classe, i);
			i++;
		}
	}
	
	public void confusionMatrix(List<String> predictions, List<String> gt){
		D1Matrix64F conf = new DenseMatrix64F(classes.size(), classes.size());
		for (String i : predictions){
			for (String j : gt){
				conf.set(classes.get(i), classes.get(j),0);
			}
		}
		for (int i = 0; i < gt.size(); i++){
			int idPred = classes.get(predictions.get(i));
			int idGt = classes.get(gt.get(i));
			conf.set(idPred, idGt, conf.get(idPred, idGt) + 1);
		}
		MatrixVisualization.show(conf, "Confusion Matrix");
	}
	
	@Override
	public double[] psi(double[] x, String y) {
		
		
		double[] p = new double[this.classes.size() * x.length];
		for (int i = 0 ; i < x.length * this.classes.get(y) ; i++){
			p[i] = 0;
		}
		for (int i = 0; i < x.length; i++){
			p[i + x.length * this.classes.get(y)] = x[i];
		}
		/*
		for (int i = x.length * this.classes.get(y)     ; i < x.length * (this.classes.get(y)+1) ; i++){
			p[i] = x[i - (x.length * this.classes.get(y))];
		}
		*/
		for (int i = x.length * (this.classes.get(y)+1) ; i < p.length ; i++){
			p[i] = 0;
		}
		return p;
	}

	@Override
	public double delta(String y, String yi) {
		if (this.classes.get(y) == this.classes.get(yi)){
			return 0;
		}
		return 1;
	}

	@Override
	public Set<String> enumerateY() {
		return classes.keySet();
	}

}
