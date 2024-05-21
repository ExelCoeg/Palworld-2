package Items;

import Monsters.*;

public class HealthPotion extends Potion{
    public void UseItem(Monster monster){
        monster.Heal();
    }
}