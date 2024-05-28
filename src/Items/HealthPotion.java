package Items;

import Monsters.Monster;
public class HealthPotion extends Potion {
    private final int healAmount = 25;
    
    public int getHealAmount() {
        return this.healAmount;
    }

    @Override
    public void useItem(Monster playerMonster) {
        playerMonster.setHp(playerMonster.getHp() + healAmount);
        System.out.println("Player used a Health Potion. HP increased by " + healAmount + ".");
    }
}
