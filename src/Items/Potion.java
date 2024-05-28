package Items;
import Monsters.Monster;
public abstract class Potion {
    public int price;
    
    public Potion() {}

    public abstract void useItem(Monster monster);

}
