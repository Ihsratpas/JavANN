package ml;

public class MNISTInitializer {

	public static void main(String[] args) throws Exception {
		
		int neurons[] = {784,30,10};
		int trainTime = 50000,printError = 1000,save = 5000,batchSize = 50;
		double learningRate = (float)1e-3;
		double[] fakeTrain[] = new double[100][784];
		MLP2 mnist = new MLP2(neurons,"softmax","CE");
		mnist.getData(fakeTrain);
		mnist.getData(mnist.toOneHot("mnist_train_100.csv",100));
		mnist.createNetwork();
		for(int i = 0;i < trainTime;i++) {
			mnist.trainSGD(learningRate,batchSize);
			if(i % printError == 0) {
				System.out.println("Iteration: " + i);
				System.out.println("Loss: " + mnist.getCost());
			}
			if(i != 0 && i % save == 0) {
				mnist.save("C:/Users/Babtu/Documents/Models/MNISTV1.ser");
			}
		}
	}

}
