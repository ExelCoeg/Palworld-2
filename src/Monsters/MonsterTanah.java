package Monsters;
public class MonsterTanah extends Monster implements ElementalMonster{
    public MonsterTanah(String nama, int level, int hp, int exp, elements elemen){
        super(nama, level, hp, exp, elemen);
    }
    
    @Override
    public void ElementalAttack(Monster monsterLawan){
       if(monsterLawan.getElemen() == Monster.elements.AIR){
        monsterLawan.setHp(monsterLawan.getHp() - (int)(0.1 * monsterLawan.getHp()));

       }
       else{
            System.out.println("Serangan tidak efektif!");
       }
    }
    
}
