package ml;

public class OtherTest {

	public static void main(String[] args) throws Exception {

		int neurons[] = {2,1};
		double[] data[] = {{(double)1,(double)1,(double)0,(double)1,(double)0},
						   {(double)1,(double)0,(double)1,(double)0,(double)0},
						   {(double)0,(double)1,(double)1,(double)0,(double)1},
						   {(double)0,(double)0,(double)0,(double)1,(double)0}};
		double testing[] = {(double)11,(double)9,(double)7,(double)10};
		MLP2 bowlerHat = new MLP2(neurons,"sigmoid","CE");
		bowlerHat.createNetwork();
		bowlerHat.getCSVData("flower.csv");
		//bowlerHat.getData(data);
		
		for(int i = 0;i < 50000;i++) {
			bowlerHat.trainSGD(.1,2);
			if(Double.isNaN(bowlerHat.getCost())) {
				System.out.println("Error: NaN loss on iteration " + i);
				bowlerHat.createNetwork();
				i = 0;
			}
			if(i % 10000 == 0) {
				System.out.println(bowlerHat.getCost());
			}
		}
		bowlerHat.printOutputs(testing);
		System.out.println();
		bowlerHat.printParameters();
		
	}

}
