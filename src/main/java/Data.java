import lombok.Getter;
import lombok.Setter;

public class Data {
    @Getter
    @Setter
    int year;

    @Getter
    @Setter
    double price;

    @Getter
    @Setter
    String CPU;

    @Getter
    @Setter
    String HDD_Size;

    public Data() {
    }



    public Data(int year, double price, String CPU, String HDD_Size) {
        this.year = year;
        this.price = price;
        this.CPU = CPU;
        this.HDD_Size = HDD_Size;
    }
}
