import lombok.Getter;
import lombok.Setter;

public class NewDevice {

    @Getter
    @Setter
    String name;
    
    @Getter
    @Setter
    Data data;

    public NewDevice(){}
 
        public NewDevice(String name, Data data) {
        this.name = name;
        this.data = data;

    }

}
