public class Main {
    public static void main(String[] args) {
        //weatherAPICall weather = new weatherAPICall();
        stockAPICall stock = new stockAPICall();

        //FOR TESTING PURPOSES
        System.out.printf("%f, %f", stock.currentPrice, stock.percentChange);
    }
}