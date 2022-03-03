package com.tama.apptest;

public class Stats {
    final static int
            health = 0,
            hunger = 1,
            energy = 2,
            sleep = 3,
            poop = 4,
            happy = 5,
            weight =6;
    Stat[] stats;

    Stats(){
        stats = new Stat[7];
        for (int i = 0; i < stats.length; i++){
            stats[i] = new Stat(20000);
        }

        stats[hunger].setFlows(stats[weight], stats[weight], stats[poop], null);
        stats[energy].setFlows(stats[weight], null, null, stats[hunger]);
        stats[sleep].setFlows(stats[health], null, null, null);


    }

    void updateStats(Pet p){
        stats[hunger].add(-PetGame.gameSpeed);
        if (stats[energy].getProp() < 1){
            stats[energy].add(PetGame.gameSpeed);
        }
        stats[sleep].add(-PetGame.gameSpeed);

    }

     class Stat {
        private float val, max;
        private Stat under, over, mirrorDown, mirrorUp;

        Stat(){
            this(20000);
        }
        Stat(float current, float maximum){
            val = current;
            max = maximum;
        }

        Stat(int maximum){
            this(maximum, maximum);
        }


        void add(float add){
            val+=add;
            if (add < 0){

                if (val < 0) {
                    if (mirrorDown != null)
                        mirrorDown.add(val - add);
                    if (under != null)
                        under.add(val);
                    val = 0;
                } else {
                    if (mirrorDown != null)
                        mirrorDown.add(-add);
                }
            } else if (add > 0){

                if (val > max){
                    if (over != null)
                        over.add(val-max);
                    if (mirrorUp != null)
                        mirrorUp.add((val - max) - add);
                    val = max;
                } else {
                    if (mirrorUp != null)
                        mirrorUp.add(-add);
                }

            }

        }


        float get(){
            return val;
        }

        float getProp(){
            return val/max;
        }

        void resetMax(float maximum){
            max = maximum;
            val = max;
        }

        void setFlows(Stat underflow, Stat overflow, Stat mirrorUp, Stat mirrorDown){
            under = underflow;
            over = overflow;
            this.mirrorDown = mirrorDown;
            this.mirrorUp = mirrorUp;
        }

     }

}
