package upmc.ri.struct.training;

import java.util.List;
import java.util.Random;
import java.util.Set;

import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.model.IStructModel;
import upmc.ri.utils.VectorOperations;

public class SGDTrainer<X, Y> implements ITrainer<X, Y> {

	private Evaluator<X, Y> evaluator;
	private double eps;
	private double lambda;
	private int max_iter;

	public SGDTrainer(Evaluator<X, Y> evaluator, double eps, double lambda, int max_iter) {
		this.evaluator = evaluator;
		this.eps = eps;
		this.lambda = lambda;
		this.max_iter = max_iter;
	}
	
	@Override
	public void  train(List<STrainingSample<X, Y>> lts , IStructModel<X,Y> model) {	
		Random rand = new Random();

		// w <- 0
		double[] parameters = model.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = 0.0;
		}
		
		IStructInstantiation<X, Y> instantiation = model.instantiation();
		
		/* Pour t = 1,...,T */
		for (int t = 0; t < this.max_iter; t++) {
			/* Pour i = 1,...,N */
			for (int i = 0; i < lts.size(); i++) {
				/* Selection aléatoire d'une paire d'apprentissage */
				STrainingSample<X, Y> ts = lts.get(rand.nextInt(lts.size()));
				X xi = ts.input;
				Y yi = ts.output;
				
				/* ŷ <- loss-augmented inference */
				Y lai = model.lai(ts);
				
				/* calcul du gradient */
				double[] psi_lai = instantiation.psi(xi, lai);
				double[] psi_yi = instantiation.psi(xi, yi);
				double[] g = VectorOperations.minus(psi_lai, psi_yi);
				
				/* mise a jour */
				for (int j = 0; j < parameters.length; j++) {
					parameters[j] = parameters[j] - this.eps * (this.lambda * parameters[j] + g[j]);
				}
			}
			this.evaluator.evaluate();
			System.out.println("epoch : " + t);
			//System.out.println("global loss : " + this.convex_loss(lts, model));
			System.out.println("err train : " + this.evaluator.getErr_train());
			System.out.println("err test : " + this.evaluator.getErr_test());
		}
	}
	
	private double convex_loss(List<STrainingSample<X, Y>> lts , IStructModel<X,Y> model) {
		double loss = 0;
		double[] parameters = model.getParameters();
		IStructInstantiation<X, Y> instantiation = model.instantiation();
		
		for (int i = 0; i < lts.size(); i++) {
			STrainingSample<X, Y> ts = lts.get(i);
			X xi = ts.input;
			Y yi = ts.output;
			double max = -9999999999.99;
			Set<Y> enumY = instantiation.enumerateY();
			for (Y y : enumY) {
				max = Math.max( max, instantiation.delta(yi, y) + VectorOperations.dot(instantiation.psi(xi, y), parameters) );
			}
			loss += max - VectorOperations.dot(instantiation.psi(xi, yi), parameters);
		}
		loss /= lts.size();
		loss += this.lambda / 2 * VectorOperations.norm2(parameters);
		return loss;
	}
}
