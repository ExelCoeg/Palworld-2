package Items;

import Monsters.Monster;

public class ElementalPotion extends Potion {
    private final int damageAmount = 25;

    public ElementalPotion(String name, int price) {
        super(name, price);
    }

    @Override
    public void useItem(Monster enemy) {
        if (enemy != null) {
            enemy.setHp(enemy.getHp() - damageAmount);
            System.out.println("Player used a Damage Potion. Enemy HP decreased by " + damageAmount);
        }
        
    }
}

