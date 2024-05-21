package Monsters;
public class MonsterTanah extends Monster implements ElementalMonster{
    public MonsterTanah(String nama, int level, int hp, int exp, elements elemen){
        super(nama, level, hp, exp, elemen);
    }
    
    @Override
    public void ElementalAttack(Monster monsterLawan){
       if(monsterLawan.getElemen() == Monster.elements.AIR){
            System.out.println("Serangan efektif!");
       }
       else{
            System.out.println("Serangan tidak efektif!");
       }
    }
    
}
