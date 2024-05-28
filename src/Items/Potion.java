package Items;
import Monsters.Monster;
public abstract class Potion {
    public int price;

    public Potion(int price) {
        this.price = price;
    }

    public abstract void useItem(Monster monster);

}
