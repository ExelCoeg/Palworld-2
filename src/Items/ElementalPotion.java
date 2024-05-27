package Items;

import Game.Player;
import Monsters.Monster;

public class ElementalPotion extends Potion {
    // private int damageAmount;

    public ElementalPotion(String name, int price) {
        super(name, price);
    }

    @Override
    public void useItem(Player player, Monster enemy) {
        // if (enemy != null) {
        //     enemy.decreaseHp(damageAmount);
        //     System.out.println("Player used a Damage Potion. Enemy HP decreased by " + damageAmount);
        }
    }

