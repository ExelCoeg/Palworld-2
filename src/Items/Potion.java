package Items;
import Monsters.Monster;
public abstract class Potion {
    public String name;
    public int price;

    public Potion(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public abstract void useItem(Monster monster);

}
