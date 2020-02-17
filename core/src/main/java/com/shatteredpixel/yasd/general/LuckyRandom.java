package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LuckyRandom {

    public static float luckFactor( Hero target ){
        return (float) Math.pow(1.05, target.Luck);
    }

    public static int rolls() {
        return rolls(Dungeon.hero);
    }

    public static int rolls(Hero hero) {
        int baseRolls = (int) luckFactor(Dungeon.hero);
        int rolls = baseRolls;
        if (Random.Float() < (luckFactor(Dungeon.hero) - baseRolls)) {
            rolls++;
        }
        if (rolls <= 0) {
            return 1;
        }
        return rolls;
    }

    public static float distance(float number1, float number2) {
        return Math.abs(number1 - number2);
    }

    public static float closest(ArrayList<Float> numbers, float target) {
        float closest = -1;
        float closestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < numbers.size(); i++) {
            if (distance(numbers.get(i), target) < closestDistance) {
                closestDistance = distance(numbers.get(i), target);
                closest = numbers.get(i);
            }
        }
        return closest;
    }

    public static int closest(ArrayList<Integer> numbers, int target) {
        ArrayList<Float> floats = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            floats.add((float)numbers.get(i));
        }
        return Math.round(closest(floats, target));
    }

    //Placeholders that roll multiple times and calculate best result. These all override watabou.utils.Random functions.
    public static float Float(float target) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.Float());
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed float in the range [0, max)
    public static float Float(float max, float target ) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.Float(max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed float in the range [min, max)
    public static float Float(float min, float max, float target ) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.Float(min, max));
        }
        return closest(numbers, target);
    }

    //returns a triangularly distributed float in the range [min, max)
    public static float NormalFloat(float min, float max, float target ) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.NormalFloat(min, max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed int in the range [0, max)
    public static int Int(int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.Int(max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed int in the range [min, max)
    public static int Int(int min, int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.Int(min, max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed int in the range [min, max]
    public static int IntRange(int min, int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.IntRange(min, max));
        }
        return closest(numbers, target);
    }

    //returns a triangularly distributed int in the range [min, max]
    public static int NormalIntRange(int min, int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.NormalIntRange(min, max));
        }
        return closest(numbers, target);
    }

    public static int Chances(float[] chances, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < LuckyRandom.rolls(); i++) {
            numbers.add(Random.chances(chances));
        }
        return closest(numbers, target);
    }
}
