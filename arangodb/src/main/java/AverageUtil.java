public class AverageUtil {
    private float sum=0;
    private int count=0;
    public float calculate(){
        return sum/count;
    }
    public void addElement(float element){
        sum+=element;
        count++;
    }
}
