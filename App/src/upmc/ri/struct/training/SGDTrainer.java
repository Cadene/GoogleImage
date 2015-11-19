package upmc.ri.struct.training;

import java.util.List;
import java.util.Random;

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
		double[] parameters = model.getParameters();
		IStructInstantiation<X, Y> instantiation = model.instantiation();
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = 0.0;
		}
		for (int t = 0; t < this.max_iter; t++) {
			for (int i = 0; i < lts.size(); i++) {
				int index = rand.nextInt((lts.size() - 0) + 1) + 0;
				STrainingSample<X, Y> ts = lts.get(index);
				X xi = ts.input;
				Y yi = ts.output;
				Y lai = model.lai(ts);
				double[] psi_lai = instantiation.psi(xi, lai);
				double[] psi_yi = instantiation.psi(xi, yi);
				double[] gi = VectorOperations.minus(psi_lai, psi_yi);
				for (int j = 0; j < parameters.length; j++) {
					parameters[j] = parameters[j] - this.eps * (this.lambda * parameters[j] + gi[j]);
				}
			}
			this.evaluator.evaluate();
			System.out.println("epoch : " + t);
			System.out.println("global loss : " + this.convex_loss(lts, model));
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
			for (Y y : instantiation.enumerateY()) {
				max = Math.max( max, instantiation.delta(yi, y) + VectorOperations.dot(instantiation.psi(xi, y), parameters) );
			}
			loss += max - VectorOperations.dot(instantiation.psi(xi, yi), parameters);
		}
		loss /= lts.size();
		loss += this.lambda / 2 * VectorOperations.norm2(parameters);
		return loss;
	}
}