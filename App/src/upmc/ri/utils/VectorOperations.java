package upmc.ri.utils;

public class VectorOperations {

	public static double[] init(int dim, double scalar) {
		double[] v = new double[dim];
		for (int i=0; i < dim; i++) {
			v[i] = scalar;
		}
		return v;
	}
	
	public static double dot(double [] v1 , double [] v2){
		double res = 0.0;
		for(int i=0;i<v1.length;i++){
			res+= v1[i]*v2[i];
		}
		return res;
	}
	
	public static double norm2(double[]v){
		return dot(v,v);
	}
	public static double norm(double[]v){
		return Math.sqrt(norm2(v));
	}
	
	public static double[] minus(double [] v1 , double [] v2){
		double[] res = v1.clone();
		for(int i=0;i<v1.length;i++){
			res[i] -= v2[i];
		}
		return res;
	}
	
	public static double[] add(double [] v1 , double [] v2){
		double[] res = v1.clone();
		for(int i=0;i<v1.length;i++){
			res[i] += v2[i];
		}
		return res;
	}
	
	public static double[] mult(double scalar, double[] v1) {
		double [] res = v1.clone();
		for (int i=0; i<v1.length; i++) {
			res[i] *= scalar;
		}
		return res;
	}
	
	public static double[] mult(int scalar, double[] v1) {
		return mult((double) scalar, v1);
	}
}
