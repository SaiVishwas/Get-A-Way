package india.collageapp.com.get_a_way;

// This is class that forms the class view
public class State {

    String name;
    String des;
    int id;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public State(String name,String des, int id) {
        this.name = name;
        this.id = id;
        this.des = des;
    }
}