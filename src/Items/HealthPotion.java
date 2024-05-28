package Items;

import Monsters.Monster;
public class HealthPotion extends Potion {
    private final int healAmount = 25;

    public int getHealAmount() {
        return this.healAmount;
    }


    public HealthPotion(String name, int price) {
        super(name, price);
    }

    @Override
    public void useItem(Monster playerMonster) {
        playerMonster.setHp(playerMonster.getHp() + healAmount);
        System.out.println("Player used a Health Potion. HP increased by " + healAmount + ".");
    }
}
