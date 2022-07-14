package com.tama.apptest;

class Rand{

    static int RandInt(int min, int max){

        return (int)(Math.random()*(max-min) + min);
    }

    static float RandFloat(float min, float max){

        return (float)(Math.random()*(max-min) + min);
    }
}

class Vec2<T> implements java.io.Serializable{
    T x, y;
    Vec2(T x, T y){
        this.x = x;
        this.y = y;
    }

    void set(T x, T y){
        this.x = x;
        this.y = y;
    }
    void set(Vec2<T> other){
        x = other.x;
        y = other.y;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Vec2))
            return false;
        Vec2 v = (Vec2)other;
        return v.x.equals(x) && v.y.equals(y);
    }

    @Override
    public String toString(){
        return "{ " + x.toString() + ", " + y.toString() + " }";
    }
}

public class A{

    final static float TAU = (float)(2*Math.PI);

    static boolean inRange(Object[][] arr, int x, int y) {
        return !(x < 0 || y < 0 || x > arr.length -1 || y > arr[x].length -1);
    }

    static boolean inRange(Object[] arr, int idx){
        return !(idx < 0 || idx > arr.length-1);
    }


}