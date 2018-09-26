import ml.MLP;

public class TFMNISTInitialize {

	public static void main(String[] args) throws Exception {
		
		int neurons[] = {784,10};
		float[] fakeTrain[] = new float[59999][794];
		float[] fakeTest[] = new float[9999][794];
		MLP mnist = new MLP(neurons,"softmax","CE");
		mnist.getData(fakeTrain);
		mnist.getData(mnist.normalize(mnist.toOneHot("mnist_train.csv",59999),255));
		mnist.getTestData(fakeTest);
		mnist.getTestData(mnist.normalize(mnist.toOneHot("mnist_test.csv",9999),255));
		mnist.createNetwork();
		mnist.save("C:/Users/Babtu/Documents/Models/TFMNISTNew.ser");
		System.out.println("Done!");
		
	}

}
